# Brproject

Emulador de servidor Lineage 2 Interlude (C6) desenvolvido em Java/Kotlin, com foco em performance, segurança e extensibilidade. Baseado na comunidade L2JBrasil, com arquitetura moderna utilizando Kotlin Coroutines, Netty, e JDK 25.

## Visão Geral

O Brproject é um emulador completo de servidor de Lineage 2, composto por:

- **GameServer** — servidor principal do jogo (porta 7777)
- **LoginServer** — servidor de autenticação (porta 2106)
- **Dashboard/Launcher** — interface GUI (Swing) para gerenciamento e licenciamento
- **Sistema de Extensões** — carregamento dinâmico de mods via ExtensionLoader

## Equipe

**Core Team:** Dhousefe-L2JBR, Agazes33, Ban-L2jDev, Warman, SrEli, Dev A.L.N

**Colaboradores:** Nattan Felipe, Diego Fonseca, Junin, ColdPlay, Denky, MecBew, Eduardo.SilvaL2J, biLL, xpower, xTech, kakuzo, Tiagorosendo, Schuster, LucasStark, damedd

## Requisitos

- **JDK 25** (Eclipse Adoptium recomendado)
- **MariaDB** (driver 3.4.0 incluído)
- **Gradle 8+** (wrapper incluído)
- **Windows 10+** ou **Linux** (Docker disponível)
- ~2 GB RAM mínimo para GameServer

## Estrutura do Projeto

```
Brproject/
├── java/                    # Código-fonte Java (2842 arquivos)
│   └── ext/mods/
│       ├── gameserver/      # Core do GameServer
│       │   ├── model/       # Atores, itens, mundo, zonas
│       │   ├── network/     # Pacotes cliente/servidor (207 client, 282 server)
│       │   ├── handler/     # Admin, bypass, chat, item, skill, voice commands
│       │   ├── scripting/   # Quests (343+), tasks, scripts de teleport
│       │   ├── skills/      # Efeitos, condições, fórmulas de combate
│       │   ├── data/        # Managers, XML parsers, SQL tables
│       │   ├── geoengine/   # Geodata, pathfinding cache
│       │   ├── communitybbs/# Community Board (BBS)
│       │   ├── custom/      # Dados customizados (balance, donate, PvP, etc.)
│       │   └── enums/       # Enumerações do jogo
│       ├── loginserver/     # LoginServer, GameServerThread
│       ├── security/        # Licenciamento, GUI launcher
│       ├── protection/      # HWID, criptografia de pacotes (VMPC)
│       ├── Crypta/          # Classes criptografadas (proteção de código)
│       ├── fakeplayer/      # Sistema de FakePlayers
│       ├── dungeon/         # Sistema de Dungeons (Kamaloka)
│       ├── sellBuffEngine/  # Sistema de venda de buffs
│       ├── tour/            # Sistema de torneios
│       ├── BossZerg/        # Anti-zerg para boss raids
│       └── ...              # Outros mods (agathion, dressme, roulette, etc.)
├── kotlin/                  # Código-fonte Kotlin (34 arquivos)
│   └── ext/mods/
│       ├── commons/pool/    # CoroutinePool, ThreadProvider
│       └── gameserver/
│           ├── GameServer.kt        # Entry-point principal
│           ├── LoginServerThread.kt # Comunicação Login<->Game
│           ├── geoengine/           # GeoEngine, PathFinder avançado
│           └── model/actor/move/    # Sistema de movimentação (Player, NPC, Summon)
├── game/
│   ├── config/              # 25 arquivos de configuração (.properties/.ini)
│   └── data/
│       ├── xml/             # 485 XMLs (items, npcs, skills, zones, spawns)
│       ├── custom/mods/     # Dados de mods customizados
│       ├── locale/          # i18n (pt-BR, en_US, ru_RU)
│       ├── prevention/crypta/ # Classes criptografadas para produção
│       └── geodata/         # Dados geográficos do mundo
├── login/                   # Configuração do LoginServer
│   └── config/              # loginserver.properties, banned_ips
├── tools/
│   └── sql/                 # 86 scripts SQL (schema do banco)
├── libs/                    # JARs de dependências
├── docs/                    # Documentação técnica (28 documentos)
├── Hwid/                    # Proteção anti-cheat (HWID)
├── images/                  # Imagens da interface
├── sound/                   # Sons do servidor
├── cache/                   # Scripts de cache AppCDS
├── build.gradle.kts         # Build system (Gradle/Kotlin DSL)
├── Mount.xml                # Build alternativo (Ant)
├── Dockerfile               # Container Docker (Alpine + JRE 21)
└── entrypoint.sh            # Script de inicialização Docker
```

## Tecnologias e Dependências

| Tecnologia | Versão | Uso |
|---|---|---|
| Kotlin | 2.3.0-Beta2 | GameServer core, GeoEngine, Pathfinding |
| Java | 25 | Maior parte do codebase |
| Kotlin Coroutines | 1.9.0 | Carregamento paralelo, thread pool |
| Netty | 4.1.107 | Networking de alta performance |
| MariaDB JDBC | 3.4.0 | Conexão com banco de dados |
| HikariCP | 5.1.0 | Connection pooling |
| FastUtil | 8.5.18 | Coleções primitivas otimizadas |
| LMAX Disruptor | 3.4.4 | Ring buffer para eventos |
| Zstd | 1.5.6 | Compressão de dados |
| Cap'n Proto | 0.1.16 | Serialização de alto desempenho |
| DeepL API | - | Tradução automática em runtime |

## Build

### Gradle (recomendado)

```bash
# Build completo (compila + gera server.jar + sincroniza bin/)
./gradlew build

# Apenas compilar
./gradlew compileJava compileKotlin

# Gerar server.jar (fat JAR)
./gradlew jar

# Copiar dependências Maven para libs/
./gradlew copyDependencies

# Gerar distribuição de teste
./gradlew PrepararTeste

# Criptografar classes sensíveis
./gradlew encryptCryptaClasses

# Build de security-tools.jar
./gradlew buildSecurityTools
```

### Ant (build legado)

```bash
# Distribuição segura (com criptografia)
ant -f Mount.xml dist-secure

# Distribuição de teste (leve)
ant -f Mount.xml dist-test

# Gerar arquivo 7z
ant -f Mount.xml dist-7z
```

## Execução

### Windows

```bat
REM Inicia via Launcher (com GUI e licenciamento)
StartBrproject.bat

REM Inicia Game sem dashboard
StartGame_SemDashboard.bat

REM Inicia Login sem dashboard
StartLogin_SemDashboard.bat

REM Registrar GameServer
RegisterGameServer.bat
```

### Docker

```bash
docker build -t brproject .
docker run -d -p 7777:7777 -p 2106:2106 \
  -e L2_EMAIL="seu@email.com" \
  -e PASSWORD="suasenha" \
  brproject
```

O container executa LoginServer e GameServer simultaneamente com flags JVM otimizadas para G1GC, AppCDS e Compact Object Headers (JDK 25+).

### Linux

O script `entrypoint.sh` pode ser usado diretamente:

```bash
chmod +x entrypoint.sh
./entrypoint.sh
```

## Configuração

Os arquivos de configuração ficam em `game/config/`:

| Arquivo | Descrição |
|---|---|
| `server.properties` | IP, portas, limites de conexão |
| `rates.properties` | Taxas de XP, SP, drop, adena |
| `players.properties` | Configurações de jogadores |
| `npcs.properties` | Comportamento de NPCs e mobs |
| `events.properties` | Eventos (TvT, DM, CTF, LM) |
| `geoengine.properties` | Geodata e pathfinding |
| `mods.properties` | Toggle de mods customizados |
| `protection.properties` | HWID, anti-cheat |
| `bosszerg.properties` | Anti-zerg em boss raids |
| `kamaloka.properties` | Sistema de dungeons |
| `offlineshop.properties` | Lojas offline |
| `items.properties` | Enchant, drop, crafting |
| `clans.properties` | Sistema de clãs |
| `siege.properties` | Configurações de siege |

O LoginServer usa `login/config/loginserver.properties`.

## Banco de Dados

O projeto usa MariaDB. Os 86 scripts SQL em `tools/sql/` criam o schema necessário:

```bash
# Windows
tools/install_db.bat

# Linux
tools/install_db.sh
```

## Sistemas e Mods Customizados

### Gameplay
- **AutoFarm** — sistema completo com rotas, skills, e controle de tempo
- **Fake Players** — bots de PvP com equipes e nomes configuráveis
- **Dungeons (Kamaloka)** — instâncias com templates XML
- **Torneios** — eventos PvP automatizados
- **Boss Zerg Protection** — anti-zerg com flag PvP automático
- **Level Up Maker** — progressão customizada de level

### Economia
- **Sell Buff Engine** — venda de buffs entre jogadores
- **Auction House** — leilões via Community Board
- **Donate System** — integração com sistema de doações
- **Capsule Box** — caixas de recompensas
- **Roulette** — roleta de prêmios

### Social
- **Community Board (BBS)** — fórum in-game, mail, favoritos
- **DressMe** — sistema de aparência/skin
- **Agathion** — companheiros com efeitos de teleport
- **Player God** — sistema de deificação

### Combat & Balance
- **Balance por Classe** — ajuste fino de dano/defesa por ClassId
- **PvP Data** — rankings e recompensas PvP
- **Enchant Data** — taxas e limites de enchant customizados
- **Equipment Grade Restriction** — restrições por grau de equip
- **Polymorph** — transformações

### Eventos
- **Capture the Flag (CTF)**
- **Death Match (DM)**
- **Last Man Standing (LM)**
- **Team vs Team (TvT)**
- **Random Farm Events**
- **Battle Boss** — eventos de boss com countdown

### Infraestrutura
- **GeoEngine + PathFinder** — pathfinding avançado com JPS+, R-Tree, suavização Catmull-Rom
- **Coroutine Pool** — thread pool baseado em Kotlin Coroutines
- **JVM Optimizer** — otimizações automáticas para JDK 25 (AppCDS, Compact Headers, THP)
- **HWID Protection** — anti-multi-client com criptografia VMPC
- **License Validator** — sistema de licenciamento por IP/email/chave
- **DeepL Translator** — tradução automática de conteúdo em runtime
- **Extension Loader** — carregamento dinâmico de módulos (.ext.jar)
- **Email Delivery** — sistema de e-mail in-game com proteção de itens

## Pathfinding e Movimentação

O sistema de pathfinding é uma das áreas mais avançadas do projeto, escrito em Kotlin:

- **AdvancedPathFinder** — JPS+ (Jump Point Search Plus) com R-Tree spatial index
- **SmoothObstacleAvoidance** — suavização de rotas com curvas Catmull-Rom
- **DynamicObstacleLayer** — obstáculos dinâmicos em runtime
- **PeaceZoneCollisionManager** — colisão de peace zones
- **CreatureMove/PlayerMove/NpcMove** — movimentação especializada por tipo de ator
- **PathfinderCache** — cache de caminhos para performance
- **L2BREngine** — motor proprietário de pathfinding


## Segurança

- Sistema de HWID previne multi-client

## Portas

| Porta | Serviço |
|---|---|
| 7777 | GameServer |
| 2106 | LoginServer |

## Documentação

A pasta `docs/` contém 28 documentos técnicos detalhados cobrindo:

- Sistema de movimentação e pathfinding
- Otimizações de performance (Coroutine Pool, JVM)
- Changelog de versões
- Fluxogramas de ataque e cast
- Sistema de colisão de criaturas
- Migração do PlayerAI
- AutoFarm (melhorias e rotas)

## Licença

GNU General Public License v3.0 — veja [LICENSE](LICENSE).

---

**Versão:** 2.9.8  
**Build:** 2026  
**Comunidade:** [L2JBrasil.com](https://l2jbrasil.com)
