package com.shtruz.externalfinalscounter.instrument.transformer.transformers;

import com.shtruz.externalfinalscounter.instrument.transformer.CustomClassWriter;
import com.shtruz.externalfinalscounter.instrument.transformer.Transformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.security.ProtectionDomain;
import java.util.List;

import static com.shtruz.externalfinalscounter.mapping.Mappings.*;
import static org.objectweb.asm.Opcodes.*;

public class GuiNewChatTransformer implements Transformer {
    @Override
    public byte[] transform(ClassLoader loader, String name, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] bytes) {
        if (classBeingRedefined == guiNewChatClass) {
            ClassReader classReader = new ClassReader(bytes);
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, 0);
            for (MethodNode method : (List<MethodNode>) classNode.methods) {
                if (method.name.equals(printChatMessageMethod.getName())
                        && method.desc.equals("(L" + iChatComponentClass.getName().replace('.', '/') + ";)V")) {
                    method.instructions.insert(onPrintChatMessage());

                    break;
                }
            }
            ClassWriter classWriter = new CustomClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        }

        return bytes;
    }

    private InsnList onPrintChatMessage() {
        InsnList insnList = new InsnList();

        insnList.add(new FieldInsnNode(GETSTATIC, "com/shtruz/externalfinalscounter/ExternalFinalsCounter", "instance", "Lcom/shtruz/externalfinalscounter/ExternalFinalsCounter;"));
        insnList.add(new VarInsnNode(ALOAD, 1));
        insnList.add(new MethodInsnNode(INVOKEVIRTUAL, "com/shtruz/externalfinalscounter/ExternalFinalsCounter", "onPrintChatMessage", "(Ljava/lang/Object;)V", false));

        return insnList;
    }
}
