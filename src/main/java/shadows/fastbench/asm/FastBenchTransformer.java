package shadows.fastbench.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import shadows.fastbench.net.PlayerListHook;

import static org.objectweb.asm.Opcodes.*;

public class FastBenchTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (basicClass == null) return null;

        if (transformedName.equals("net.minecraft.server.management.PlayerList")) {
            System.out.println("Patching " + transformedName);
            return patchPlayerList(basicClass);
        }

        return basicClass;
    }

    private byte[] patchPlayerList(byte[] basicClass) {
        ClassReader reader = new ClassReader(basicClass);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);

        for (MethodNode method : node.methods) {
            if (method.name.equals("initializeConnectionToPlayer") && method.desc.equals("(Lnet/minecraft/network/NetworkManager;Lnet/minecraft/entity/player/EntityPlayerMP;Lnet/minecraft/network/NetHandlerPlayServer;)V")) {
                InsnList list = new InsnList();
                list.add(new VarInsnNode(ALOAD, 2));
                list.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(PlayerListHook.class), "initializeConnectionToPlayerHook", "(Lnet/minecraft/entity/player/EntityPlayerMP;)V", true));
                method.instructions.insert(list);
            }
        }

        ClassWriter writer = new ClassWriter(0);
        node.accept(writer);
        return writer.toByteArray();
    }
}
