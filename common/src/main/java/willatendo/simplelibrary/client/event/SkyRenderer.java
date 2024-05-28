package willatendo.simplelibrary.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.util.profiling.ProfilerFiller;
import org.joml.Matrix4f;

public interface SkyRenderer {
    LevelRenderer levelRenderer();

    PoseStack poseStack();

    float tickDelta();

    long limitTime();

    boolean blockOutlines();

    Camera getCamera();

    GameRenderer getGameRenderer();

    LightTexture getLightTexture();

    Matrix4f getProjectionMatrix();

    ClientLevel getLevel();

    ProfilerFiller getProfilerFiller();

    boolean advancedTranslucency();

    MultiBufferSource getMultiBufferSource();

    Frustum getFrustum();
}
