function initializeCoreMod() {
	return {
		'fbplayertransfer': {
			'target': {
				'type': 'METHOD',
				'class': 'net.minecraft.inventory.container.PlayerContainer',
				'methodName': '<init>',
				'methodDesc': '(Lnet/minecraft/entity/player/PlayerInventory;ZLnet/minecraft/entity/player/PlayerEntity;)V'
			},
			'transformer': function(method) {
                var owner = "shadows/fastbench/asm/PlayerContainerHooks";
                var name = "updatePlayerContainer";
                var desc = "(Lnet/minecraft/inventory/container/PlayerContainer;)V";
                var instr = method.instructions;

                var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
				var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
				var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
				var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');
                var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
                var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
				
				ASMAPI.log('INFO', 'Patching PlayerContainer#<init>');

                var insn = new InsnList();
                insn.add(new VarInsnNode(Opcodes.ALOAD, 0));
                insn.add(ASMAPI.buildMethodCall(
                    owner,
                    name,
                    desc,
                    ASMAPI.MethodType.STATIC));
                instr.insertBefore(instr.getLast().getPrevious(), insn);

                return method;
            }
		}
	}
}