function initializeCoreMod() {
	return {
		'fbplayertransfer': {
			'target': {
				'type': 'METHOD',
				'class': 'net.minecraft.inventory.container.PlayerContainer',
				'methodName': 'func_82846_b',
				'methodDesc': '(Lnet/minecraft/entity/player/PlayerEntity;I)Lnet/minecraft/item/ItemStack;'
			},
			'transformer': function(method) {
				ASMAPI.log('[FastWorkbench]: Patching PlayerContainer#transferStackInSlot');

                var owner = "shadows/fastbench/asm/PlayerContainerHooks";
                var name = "transferStackInSlot";
                var desc = "(Lnet/minecraft/inventory/container/PlayerContainer;)Lnet/minecraft/item/ItemStack;";
                var instr = method.instructions;

                var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
				var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
				var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
				var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');
                var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
                var InsnList = Java.type('org.objectweb.asm.tree.InsnList');

                var insn = new InsnList();
				var label = new LabelNode();
				insn.add(new VarInsnNode(Opcodes.ILOAD, 2));
				insn.add(new JumpInsnNode(Opcodes.IFNE, label));
                insn.add(new VarInsnNode(Opcodes.ALOAD, 0));
                insn.add(ASMAPI.buildMethodCall(
                    owner,
                    name,
                    desc,
                    ASMAPI.MethodType.STATIC));
                insn.add(new InsnNode(Opcodes.ARETURN));
				insn.add(label);
                instr.insert(insn);

                return method;
            }
		}
	}
}