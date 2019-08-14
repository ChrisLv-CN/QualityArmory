package me.zombie_striker.qg.handlers.chargers;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.zombie_striker.customitemmanager.OLD_ItemFact;
import me.zombie_striker.qg.QAMain;
import me.zombie_striker.qg.api.QualityArmory;
import me.zombie_striker.qg.guns.Gun;
import me.zombie_striker.qg.guns.utils.GunUtil;

public class DelayedBurstFireCharger implements ChargingHandler {

	public static HashMap<UUID, BukkitTask> shooters = new HashMap<>();

	public DelayedBurstFireCharger() {
		ChargingManager.add(this);
	}

	@Override
	public String getName() {
		return ChargingManager.DelayedBURSTFIRE;
	}

	@Override
	public boolean isCharging(Player player) {
		return shooters.containsKey(player.getUniqueId());
	}

	@Override
	public boolean isReadyToFire(final Gun g, final Player player, final ItemStack stack) {
		return true;
	}

	@Override
	public boolean useChargingShoot() {
		return true;
	}

	@Override
	public void shoot(final Gun g, final Player player, final ItemStack stack) {
		QAMain.DEBUG("DelayedBurstFireCharger shoot.");
//		GunUtil.shootHandler(g, player, 1);
//		GunUtil.playShoot(g, player);

		shooters.put(player.getUniqueId(), new BukkitRunnable() {
			int slotUsed = player.getInventory().getHeldItemSlot();
			@SuppressWarnings("deprecation")
			boolean offhand = QualityArmory.isIronSights(player.getItemInHand());
			int shotCurrently = 0;

			int currentRate = (int) (10 / g.getFireRate() / Math.pow(2, g.getBulletsPerShot()));
			int skippedTicks = 0;

			@Override
			@SuppressWarnings("deprecation")
			public void run() {
				if (skippedTicks >= currentRate) {
					skippedTicks = 0;
					currentRate *= 2;
				} else {
					skippedTicks++;
					return;
				}
				int amount = Gun.getAmount(stack);
				if (shotCurrently >= g.getBulletsPerShot() || slotUsed != player.getInventory().getHeldItemSlot()
						|| amount <= 0) {
					if (shooters.containsKey(player.getUniqueId()))
						shooters.remove(player.getUniqueId()).cancel();
					return;
				}

				GunUtil.shootHandler(g, player, 1);
				GunUtil.playShoot(g, player);
				if (QAMain.enableRecoil && g.getRecoil() > 0) {
					GunUtil.addRecoil(player, g);
				}
				shotCurrently++;
				GunUtil.reduceOneAmmo(player, stack, offhand, g);
//				amount--;
//
//				if (amount < 0)
//					amount = 0;
//
//				// if (QAMain.enableVisibleAmounts) {
//				// stack.setAmount(amount > 64 ? 64 : amount == 0 ? 1 : amount);
//				// }
//				ItemMeta im = stack.getItemMeta();
//				int slot;
//				if (offhand) {
//					slot = -1;
//				} else {
//					slot = player.getInventory().getHeldItemSlot();
//				}
//				im.setLore(Gun.getGunLore(g, stack, amount));
//				stack.setItemMeta(im);
//				if (slot == -1) {
//					try {
//						if (QualityArmory.isIronSights(player.getItemInHand())) {
//							player.getInventory().setItemInOffHand(stack);
//						} else {
//							player.getInventory().setItemInHand(stack);
//						}
//
//					} catch (Error e) {
//					}
//				} else {
//					player.getInventory().setItem(slot, stack);
//				}
//				QualityArmory.sendHotbarGunAmmoCount(player, g, stack, false);
			}
		}.runTaskTimer(QAMain.getInstance(), 1, 1));
	}

}
