/*
 * Copyright (c) Dev A.L.N. Todos os direitos reservados.
 */
package ext.mods.gameserver.communitybbs.custom;

import java.util.StringTokenizer;

import ext.mods.donation.DonationPixAccess;
import ext.mods.gameserver.communitybbs.manager.BaseBBSManager;
import ext.mods.gameserver.model.actor.Player;

/**
 * Community Board → compra de Coin of Luck via PIX (mesma janela do comando {@code .pix}).
 * Bypass: {@code _bbspix} (PT) ou {@code _bbspix;pay} (EN).
 */
public class PixBBSManager extends BaseBBSManager
{
	@Override
	public void parseCmd(String command, Player player)
	{
		final StringTokenizer st = new StringTokenizer(command, " ;");
		st.nextToken(); // _bbspix
		
		final String action = st.hasMoreTokens() ? st.nextToken().toLowerCase() : "pix";
		final boolean english = "pay".equals(action) || "en".equals(action);
		DonationPixAccess.openShop(player, english ? "en" : "pt");
	}
	
	public static PixBBSManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final PixBBSManager INSTANCE = new PixBBSManager();
	}
}
