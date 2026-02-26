package etherested.patience.client.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import etherested.patience.client.CraftingHandler;

import java.util.Random;

// looping sound instance that plays during crafting and stops when crafting ends
public class CraftingSoundInstance extends AbstractTickableSoundInstance {
    private static final Random RANDOM = new Random();
    private static final float PITCH_VARIATION = 0.15F;

    private boolean forceStopped = false;

    public CraftingSoundInstance() {
        this(SoundRegistry.crafting());
    }

    public CraftingSoundInstance(SoundEvent soundEvent) {
        super(soundEvent, SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.looping = false;
        this.delay = 0;
        this.volume = 1.0F;
        this.pitch = randomizePitch();
        this.relative = true;
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
    }

    public CraftingSoundInstance(ResourceLocation soundLocation) {
        this(SoundEvent.createVariableRangeEvent(soundLocation));
    }

    public static float randomizePitch() {
        return 1.0F + (RANDOM.nextFloat() * 2 - 1) * PITCH_VARIATION;
    }

    @Override
    public void tick() {
        if (forceStopped || !CraftingHandler.getInstance().isCrafting()) {
            this.stop();
        }
    }

    public void forceStop() {
        this.forceStopped = true;
        this.stop();
    }

    public boolean isForceStopped() {
        return forceStopped;
    }
}
