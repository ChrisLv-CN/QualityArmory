package me.zombie_striker.qg.handlers.chargers;

import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.zombie_striker.qg.guns.Gun;
import me.zombie_striker.qg.guns.utils.GunUtil;
import me.zombie_striker.qg.handlers.chargers.ChargingHandler;
import me.zombie_striker.qg.handlers.chargers.ChargingManager;

public class PushbackCharger implements ChargingHandler {

	
	public PushbackCharger() {
		ChargingManager.add(this);
	}

	@Override
	public String getName() {
		return ChargingManager.PUSHBACK;
	}

	@Override
	public boolean isCharging(Player arg0) {
		return false;
	}

	@Override
	public boolean isReadyToFire(Gun g, Player player, ItemStack stack) {
		return true;
	}

	@Override
	public boolean useChargingShoot() {
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void shoot(Gun g, Player p, ItemStack i) {

		Location start = p.getEyeLocation().clone();
		Vector go = p.getLocation().getDirection().normalize();
		//go.add(new Vector((Math.random() * 2 * sway) - sway, (Math.random() * 2 * sway) - sway,
		//		(Math.random() * 2 * sway) - sway));
		GunUtil.playShoot(g,  p);

		boolean lookup = (go.getY() > go.getX() && go.getY() > go.getZ());
		boolean lookdown = (-go.getY() > go.getX() && -go.getY() > go.getZ());
		
		double degreeVector = Math.atan2(go.getX(), go.getZ());
		if (degreeVector > Math.PI)
			degreeVector = 2 * Math.PI - degreeVector;
		for (Entity e : p.getNearbyEntities(g.getMaxDistance(), g.getMaxDistance(), g.getMaxDistance())) {
			double dis = e.getLocation().distance(start);
			if (e instanceof Damageable)
				if (e != p && e != p.getVehicle() && e != p.getPassenger()) {
					double degreeEntity = Math.atan2(e.getLocation().getX() - start.getX(),
							e.getLocation().getZ() - start.getZ());
					if (degreeEntity > Math.PI)
						degreeEntity = 2 * Math.PI - degreeEntity;
					
					if ((lookup && e.getLocation().getY() > start.getY()) || (lookdown && e.getLocation().getY() < start.getY()) ||
							
							
							(!lookdown&&!lookup&&Math.max(degreeEntity, degreeVector)
							- Math.min(degreeEntity, degreeVector) < (dis > 10 ? Math.PI / 4 : Math.PI / 2))) {
						
						Vector pushback = new Vector(e.getLocation().getX() - start.getX(),e.getLocation().getY() - start.getY(),
								e.getLocation().getZ() - start.getZ());
						pushback.normalize().multiply(g.getMaxDistance()/(dis));
						e.setVelocity(pushback);
					}
				}
		}
//		return false;
	}

}
