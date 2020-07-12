function initializeCoreMod() {
	return {
		'fbplayermatrix': {
			'target': {
				'type': 'METHOD',
				'class': 'net.minecraft.inventory.container.PlayerContainer',
				'methodName': 'func_75130_a',
				'methodDesc': '(Lnet/minecraft/inventory/IInventory;)V'
			},
			'transformer': function(method) {
				print('[FastWorkbench]: Patching PlayerContainer#onCraftMatrixChanged');

                var owner = "shadows/fastbench/asm/PlayerContainerHooks";
                var name = "onCraftMatrixChanged";
                var desc = "(Lnet/minecraft/inventory/container/PlayerContainer;)V";
                var instr = method.instructions;

                var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
                var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
                var InsnList = Java.type('org.objectweb.asm.tree.InsnList');

                var insn = new InsnList();
                insn.add(new VarInsnNode(Opcodes.ALOAD, 0));
                insn.add(ASMAPI.buildMethodCall(
                    owner,
                    name,
                    desc,
                    ASMAPI.MethodType.STATIC));
                insn.add(new InsnNode(Opcodes.RETURN));
                instr.insert(insn);

                return method;
            }
		}
	}
}