package mooklabs.nausicaamod.keysticksetc;

import mooklabs.nausicaamod.ExtendedPlayer;
import mooklabs.nausicaamod.Main;
import mooklabs.nausicaamod.mobs.Ohmu;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class NausicaaEventHandler {

	// for attacks
	@SubscribeEvent
	public void entityattack(AttackEntityEvent event) {
		if (Main.debug) System.out.println(event.target.toString());
		if (event.target instanceof Ohmu) if (event.entityPlayer.worldObj.isRemote) {
			ExtendedPlayer props = ExtendedPlayer.get(event.entityPlayer);
			System.out.println("before: " + props.toString());
			props.changeKindness(-20);
			// props.changeKindness(-1);
			System.out.println("after: " + props.toString());
		}

	}

	// for falling(with glider)
	@SubscribeEvent
	public void entityfall(LivingFallEvent event) {
		if (event.entityLiving instanceof EntityPlayer) {// make sure its a player
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			if (Main.debug) System.out.println(event.distance);

			if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Main.glider) {
				
				double yMotion = Math.abs(player.motionY);//get absolute value of y motion
				if(yMotion > .2){//check how large motion is
				event.distance = (float) (yMotion) * 5;		// then compute fall distance
				if (Main.debug)System.out.println("Fall dist: " + event.distance+"\nThrust: " + yMotion);//for debugging
				}else {event.distance = 0;//if small (less than .2 bloks per ____(tick?), set to none
				player.addChatMessage(new ChatComponentText("Perfect landing!"));
				}
			}

		}
	}

	// for kindness
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
		/* Be sure to check if the entity being constructed is the correct type for the extended properties you're about to add! The null check may not be necessary - I only
		 * use it to make sure properties are only registered once per entity */
		if (event.entity instanceof EntityPlayer && ExtendedPlayer.get((EntityPlayer) event.entity) == null)
		// This is how extended properties are registered using our convenient method from earlier
		ExtendedPlayer.register((EntityPlayer) event.entity);
		// That will call the constructor as well as cause the init() method to be called automatically

		// If you didn't make the two convenient methods from earlier, your code would be much uglier:
		if (event.entity instanceof EntityPlayer && event.entity.getExtendedProperties(ExtendedPlayer.EXT_PROP_NAME) == null) event.entity.registerExtendedProperties(ExtendedPlayer.EXT_PROP_NAME,
				new ExtendedPlayer((EntityPlayer) event.entity));
	}
}