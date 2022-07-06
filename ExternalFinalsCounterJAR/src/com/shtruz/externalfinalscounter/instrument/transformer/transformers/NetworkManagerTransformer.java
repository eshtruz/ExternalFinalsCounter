package com.shtruz.externalfinalscounter.instrument.transformer.transformers;

import com.shtruz.externalfinalscounter.instrument.transformer.Transformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.security.ProtectionDomain;
import java.util.List;

import static com.shtruz.externalfinalscounter.mapping.Mappings.*;
import static org.objectweb.asm.Opcodes.*;

public class NetworkManagerTransformer implements Transformer {
    @Override
    public byte[] transform(ClassLoader loader, String name, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] bytes) {
        if (classBeingRedefined == networkManagerClass) {
            ClassReader classReader = new ClassReader(bytes);
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, 0);
            for (MethodNode method : (List<MethodNode>) classNode.methods) {
                if (method.name.equals("channelRead0")) {
                    method.instructions.insert(onPacket(2));
                } else if (method.name.equals(sendPacketMethod.getName())
                        && method.desc.equals("(L" + packetClass.getName().replace('.', '/') + ";)V")) {
                    method.instructions.insert(onPacket(1));
                }
            }
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        }

        return bytes;
    }

    private InsnList onPacket(int packetIndex) {
        InsnList insnList = new InsnList();

        insnList.add(new FieldInsnNode(GETSTATIC, "com/shtruz/externalfinalscounter/ExternalFinalsCounter", "instance", "Lcom/shtruz/externalfinalscounter/ExternalFinalsCounter;"));
        insnList.add(new VarInsnNode(ALOAD, packetIndex));
        insnList.add(new MethodInsnNode(INVOKEVIRTUAL, "com/shtruz/externalfinalscounter/ExternalFinalsCounter", "onPacket", "(Ljava/lang/Object;)Z", false));
        LabelNode ifeq = new LabelNode();
        insnList.add(new JumpInsnNode(IFEQ, ifeq));
        insnList.add(new InsnNode(RETURN));
        insnList.add(ifeq);

        return insnList;
    }
}
