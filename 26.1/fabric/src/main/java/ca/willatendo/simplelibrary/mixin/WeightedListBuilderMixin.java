package ca.willatendo.simplelibrary.mixin;

import ca.willatendo.simplelibrary.injects.WeightedListBuilderAccessor;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

@Mixin(WeightedList.Builder.class)
public class WeightedListBuilderMixin<E> implements WeightedListBuilderAccessor<E> {
    private final List<Weighted<E>> result = new ArrayList<>();

    @Inject(at = @At("HEAD"), method = "add", cancellable = true)
    private void add(E element, CallbackInfoReturnable<WeightedList.Builder<E>> cir) {
        this.result.add(new Weighted<>(element, 1));
        cir.setReturnValue((WeightedList.Builder<E>) (Object) this);
    }

    @Inject(at = @At("HEAD"), method = "add(Ljava/lang/Object;I)Lnet/minecraft/util/random/WeightedList$Builder;", cancellable = true)
    private void add(E element, int weight, CallbackInfoReturnable<WeightedList.Builder<E>> cir) {
        this.result.add(new Weighted<>(element, weight));
        cir.setReturnValue((WeightedList.Builder<E>) (Object) this);
    }

    @Inject(at = @At("HEAD"), method = "build", cancellable = true)
    private void build(CallbackInfoReturnable<WeightedList<E>> cir) {
        cir.setReturnValue(new WeightedList<>(this.result));
    }

    @Override
    public WeightedList.Builder<E> addAll(Collection<Weighted<E>> values) {
        this.result.addAll(values);
        return (WeightedList.Builder<E>) (Object) this;
    }

    @Override
    public WeightedList.Builder<E> removeIf(Predicate<Weighted<E>> filter) {
        this.result.removeIf(filter);
        return (WeightedList.Builder<E>) (Object) this;
    }
}
