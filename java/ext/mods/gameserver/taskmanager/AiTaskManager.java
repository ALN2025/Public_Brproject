/*
* Copyleft © 2024-2026 L2Brproject
* * This file is part of L2Brproject derived from aCis409/RusaCis3.8
* * L2Brproject is free software: you can redistribute it and/or modify it
* under the terms of the GNU General Public License as published by the
* Free Software Foundation, either version 3 of the License.
* * L2Brproject is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* General Public License for more details.
* * You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
* Our main Developers, Dhousefe-L2JBR, Agazes33, Ban-L2jDev, Warman, SrEli.
* Our special thanks, Nattan Felipe, Diego Fonseca, Junin, ColdPlay, Denky, MecBew, Localhost, MundvayneHELLBOY, 
* SonecaL2, Eduardo.SilvaL2J, biLL, xpower, xTech, kakuzo, Tiagorosendo, Schuster, LucasStark, damedd
* as a contribution for the forum L2JBrasil.com
 */
package ext.mods.gameserver.taskmanager;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import ext.mods.commons.pool.ThreadPool;
import ext.mods.commons.random.Rnd;
import ext.mods.Config;
import ext.mods.gameserver.enums.IntentionType;
import ext.mods.gameserver.model.actor.Npc;
import ext.mods.gameserver.model.actor.instance.Folk;
import ext.mods.gameserver.model.actor.instance.GrandBoss;
import ext.mods.gameserver.model.actor.instance.Monster;
import ext.mods.gameserver.model.actor.instance.RaidBoss;
import ext.mods.gameserver.model.actor.Player;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;

/**
 * Handle all {@link Npc} AI tasks.
 */
public final class AiTaskManager implements Runnable
{
    private final Set<Npc> _npcs = ConcurrentHashMap.newKeySet();
    
    private static final int RETURN_HOME_RAIDBOSS_RADIUS = Config.RETURN_HOME_RAIDBOSS_RADIUS;
    private static final int RETURN_HOME_MONSTER_RADIUS = Config.RETURN_HOME_MONSTER_RADIUS;
    
    private static final IntSet EXCLUDED_RAIDBOSS_IDS = IntSets.unmodifiable(new IntOpenHashSet(new int[]{ 29095 }));
    private static final IntSet EXCLUDED_MONSTER_IDS = IntSets.unmodifiable(new IntOpenHashSet(new int[]{ 29016, 29008, 29004 }));
    
    protected AiTaskManager()
    {
        ThreadPool.scheduleAtFixedRate(this, 1000, 1000);
        ThreadPool.scheduleAtFixedRate(this::animationTask, 10000, 10000);
    }
    
    @Override
    public final void run()
    {
        for (Npc npc : _npcs)
        {
            processNpc(npc);
        }
    }
    
    private void processNpc(Npc npc)
    {
        if (Config.ENABLE_NPC_MOVEMENT_OPTIMIZATION && !(npc instanceof GrandBoss || npc instanceof RaidBoss))
        {
            if (!hasVisiblePlayersNearNpc(npc))
            {
                return;
            }
        }
        
        
        if (npc instanceof GrandBoss)
            return;
        else if (npc instanceof RaidBoss raidBoss)
            monsterReturn(raidBoss, Config.RETURN_HOME_RAIDBOSS, RETURN_HOME_RAIDBOSS_RADIUS, EXCLUDED_RAIDBOSS_IDS);
        else if (npc instanceof Monster monster)
            monsterReturn(monster, Config.RETURN_HOME_MONSTER, RETURN_HOME_MONSTER_RADIUS, EXCLUDED_MONSTER_IDS);
    }
    
    private void monsterReturn(Monster monster, boolean returnHome, int radius, IntSet excludedNpcIds)
    {
        if (!returnHome || isNpcIdExcluded(monster.getNpcId(), excludedNpcIds))
            return;
        
        if (!monster.isIn3DRadius(monster.getSpawnLocation(), radius))
        {
            monster.teleportTo(monster.getSpawnLocation(), 0);
            monster.removeAllAttackDesire();
            monster.getStatus().setHpMp(monster.getStatus().getMaxHp(), monster.getStatus().getMaxMp());
            teleportMinions(monster);
        }
    }
    
    private boolean isNpcIdExcluded(int npcId, IntSet excludedNpcIds)
    {
        return excludedNpcIds.contains(npcId);
    }
    
    private void teleportMinions(Monster monster)
    {
        for (Npc minion : monster.getMinions())
        {
            if (!minion.isDead())
            {
                minion.teleportToMaster();
                minion.removeAllAttackDesire();
                minion.getStatus().setHpMp(minion.getStatus().getMaxHp(), minion.getStatus().getMaxMp());
            }
        }
    }
    
    /**
     * Verifica se há players visíveis próximos ao NPC.
     */
    private boolean hasVisiblePlayersNearNpc(Npc npc)
    {
        if (Config.NPC_MOVEMENT_PLAYER_RANGE <= 0)
            return true;
        
        for (Player player : npc.getKnownTypeInRadius(Player.class, Config.NPC_MOVEMENT_PLAYER_RANGE))
        {
            if (player.getAppearance().isVisible())
            {
                return true;
            }
        }
        return false;
    }
    
    protected final void animationTask()
    {
        for (Npc npc : _npcs)
        {
            if (!(npc instanceof Folk folk)) continue;
            
            int moveAroundSocial = folk.getTemplate().getAiParams().getInteger("MoveAroundSocial", 0);
            int moveAroundSocial1 = folk.getTemplate().getAiParams().getInteger("MoveAroundSocial1", 0);
            
            if (moveAroundSocial > 0 || moveAroundSocial1 > 0)
            {
                if (folk.getStatus().getHpRatio() > 0.4 && !folk.isDead() && folk.getAI().getCurrentIntention().getType() != IntentionType.ATTACK)
                {
                    if (Rnd.get(100) < Config.NPC_ANIMATION)
                    {
                        if (moveAroundSocial > 0)
                            folk.getAI().addSocialDesire(3, (moveAroundSocial * 1000) / 30, 50);
                        else
                            folk.getAI().addSocialDesire(3, (moveAroundSocial1 * 1000) / 30, 50);
                    }
                }
            }
        }
    }
    
    public final void add(Npc npc)
    {
        npc.setAISleeping(false);
        _npcs.add(npc);
    }
    
    public final void remove(Npc npc)
    {
        npc.setAISleeping(true);
        _npcs.remove(npc);
    }
    
    public static final AiTaskManager getInstance()
    {
        return SingletonHolder.INSTANCE;
    }
    
    private static final class SingletonHolder
    {
        protected static final AiTaskManager INSTANCE = new AiTaskManager();
    }
}