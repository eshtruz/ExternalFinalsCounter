package com.shtruz.externalfinalscounter.instrument.transformer.transformers;

import com.shtruz.externalfinalscounter.instrument.transformer.Transformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.security.ProtectionDomain;
import java.util.List;

import static com.shtruz.externalfinalscounter.mapping.Mappings.*;
import static org.objectweb.asm.Opcodes.*;

public class GuiPlayerTabOverlayTransformer implements Transformer {
    @Override
    public byte[] transform(ClassLoader loader, String name, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] bytes) {
        if (classBeingRedefined == guiPlayerTabOverlayClass) {
            ClassReader classReader = new ClassReader(bytes);
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, 0);
            for (MethodNode method : (List<MethodNode>) classNode.methods) {
                if (method.name.equals(renderPlayerlistMethod.getName())
                        && method.desc.equals("(IL" + scoreboardClass.getName().replace('.', '/')
                        + ";L" + scoreObjectiveClass.getName().replace('.', '/')
                        + ";)V")) {
                    for (AbstractInsnNode abstractInsnNode : method.instructions.toArray()) {
                        if (abstractInsnNode instanceof MethodInsnNode) {
                            MethodInsnNode methodInsnNode = (MethodInsnNode) abstractInsnNode;

                            if (methodInsnNode.getOpcode() == INVOKEVIRTUAL
                                    && methodInsnNode.owner.equals(guiPlayerTabOverlayClass.getName().replace('.', '/'))
                                    && methodInsnNode.name.equals(gptoGetPlayerNameMethod.getName())
                                    && methodInsnNode.desc.equals("(L" + networkPlayerInfoClass.getName().replace('.', '/')
                                    + ";)Ljava/lang/String;")) {
                                if (methodInsnNode.getPrevious() instanceof VarInsnNode) {
                                    VarInsnNode varInsnNode = (VarInsnNode) methodInsnNode.getPrevious();

                                    if (varInsnNode.getOpcode() == ALOAD) {
                                        method.instructions.insert(methodInsnNode, finalsInTab(varInsnNode.var));
                                    }
                                }
                            }
                        }
                    }

                    break;
                }
            }
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        }

        return bytes;
    }

    private InsnList finalsInTab(int networkPlayerInfoIndex) {
        InsnList insnList = new InsnList();

        insnList.add(new FieldInsnNode(GETSTATIC, "com/shtruz/externalfinalscounter/ExternalFinalsCounter", "instance", "Lcom/shtruz/externalfinalscounter/ExternalFinalsCounter;"));
        insnList.add(new MethodInsnNode(INVOKEVIRTUAL, "com/shtruz/externalfinalscounter/ExternalFinalsCounter", "getChatMessageParser", "()Lcom/shtruz/externalfinalscounter/finalscounter/ChatMessageParser;", false));
        insnList.add(new VarInsnNode(ALOAD, networkPlayerInfoIndex));
        insnList.add(new MethodInsnNode(INVOKEVIRTUAL, networkPlayerInfoClass.getName().replace('.', '/'), getGameProfileMethod.getName(), "()Lcom/mojang/authlib/GameProfile;", false));
        insnList.add(new MethodInsnNode(INVOKEVIRTUAL, "com/mojang/authlib/GameProfile", "getName", "()Ljava/lang/String;", false));
        insnList.add(new MethodInsnNode(INVOKEVIRTUAL, "com/shtruz/externalfinalscounter/finalscounter/ChatMessageParser", "getFinalsInTabString", "(Ljava/lang/String;)Ljava/lang/String;", false));
        insnList.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/String", "concat", "(Ljava/lang/String;)Ljava/lang/String;", false));

        return insnList;
    }
}
