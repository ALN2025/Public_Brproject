/*
 * Copyright (c) Dev A.L.N. Todos os direitos reservados.
 */
package ext.mods.gameserver.handler.voicedcommandhandlers;

import ext.mods.donation.DonationPixAccess;
import ext.mods.gameserver.handler.IVoicedCommandHandler;
import ext.mods.gameserver.model.actor.Player;

/**
 * Comandos <b>.pix</b> (PT) e <b>.pay</b> (EN) — abre o DonationManager in-game.
 */
public class Pix implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS =
	{
		"pix",
		"pay"
	};
	
	@Override
	public boolean useVoicedCommand(String command, Player player, String target)
	{
		return DonationPixAccess.openShop(player, "pay".equalsIgnoreCase(command) ? "en" : "pt");
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}
