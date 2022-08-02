package com.shtruz.externalfinalscounter.instrument.transformer.transformers;

import com.shtruz.externalfinalscounter.instrument.transformer.CustomClassWriter;
import com.shtruz.externalfinalscounter.instrument.transformer.Transformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.security.ProtectionDomain;
import java.util.List;

import static com.shtruz.externalfinalscounter.mapping.Mappings.entityPlayerSPClass;
import static com.shtruz.externalfinalscounter.mapping.Mappings.sendChatMessageMethod;
import static org.objectweb.asm.Opcodes.*;

public class EntityPlayerSPTransformer implements Transformer {
    @Override
    public byte[] transform(ClassLoader loader, String name, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] bytes) {
        if (classBeingRedefined == entityPlayerSPClass) {
            ClassReader classReader = new ClassReader(bytes);
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, 0);
            for (MethodNode method : (List<MethodNode>) classNode.methods) {
                if (method.name.equals(sendChatMessageMethod.getName())
                        && method.desc.equals("(Ljava/lang/String;)V")) {
                    method.instructions.insert(onSendChatMessage());

                    break;
                }
            }
            ClassWriter classWriter = new CustomClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        }

        return bytes;
    }

    private InsnList onSendChatMessage() {
        InsnList insnList = new InsnList();

        insnList.add(new FieldInsnNode(GETSTATIC, "com/shtruz/externalfinalscounter/ExternalFinalsCounter", "instance", "Lcom/shtruz/externalfinalscounter/ExternalFinalsCounter;"));
        insnList.add(new VarInsnNode(ALOAD, 1));
        insnList.add(new MethodInsnNode(INVOKEVIRTUAL, "com/shtruz/externalfinalscounter/ExternalFinalsCounter", "onSendChatMessage", "(Ljava/lang/String;)Z", false));
        LabelNode ifeq = new LabelNode();
        insnList.add(new JumpInsnNode(IFEQ, ifeq));
        insnList.add(new InsnNode(RETURN));
        insnList.add(ifeq);

        return insnList;
    }
}
