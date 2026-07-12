/*
 * Copyright (c) Dev A.L.N. Todos os direitos reservados.
 */
package ext.mods.donation;

import ext.mods.Config;
import ext.mods.commons.logging.CLogger;
import ext.mods.gameserver.model.actor.Player;
import ext.mods.gameserver.network.serverpackets.ActionFailed;
import ext.mods.gameserver.network.serverpackets.ShowBoard;

import net.sf.donationmanager.DonationManager;

/**
 * Abre a loja PIX/DonationManager — usado por {@code .pix}, Community Board e bypass {@code pix}.
 */
public final class DonationPixAccess
{
	private static final CLogger LOGGER = new CLogger(DonationPixAccess.class.getName());
	
	private DonationPixAccess()
	{
	}
	
	/**
	 * @param player jogador
	 * @param locale {@code pt} ou {@code en} (pastas HTML mods/donation/)
	 * @return false se o mod estiver desligado
	 */
	public static boolean openShop(Player player, String locale)
	{
		if (player == null)
			return false;
		
		if (!Config.ENABLE_PIX_MOD || !Config.DONATION_ENABLED)
		{
			player.sendMessage("Sistema PIX desativado.");
			return false;
		}
		
		final String lang = "en".equalsIgnoreCase(locale) ? "en" : "pt";
		player.getMemos().set(DonationManager.MEMO_DONATION_HTML_LOCALE, lang);
		DonationManager.getInstance().prepareShopForLocale(player);
		
		// Fecha o Community Board antes da janela MOD.PIX (evita conflito de HTML/pacotes).
		player.sendPacket(ShowBoard.STATIC_CLOSE);
		
		try
		{
			DonationManager.getInstance().showIndexWindow(player);
		}
		catch (Exception e)
		{
			LOGGER.warn("Falha ao abrir loja PIX para {}.", e, player.getName());
			player.sendMessage("en".equals(lang) ? "Could not open the shop. Check GameServer log." : "Nao foi possivel abrir a loja. Veja o log do GameServer.");
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}
		
		return true;
	}
}
