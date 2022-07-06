package com.shtruz.externalfinalscounter.instrument.transformer.transformers;

import com.shtruz.externalfinalscounter.instrument.transformer.Transformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.security.ProtectionDomain;
import java.util.List;

import static com.shtruz.externalfinalscounter.mapping.Mappings.*;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

public class MinecraftTransformer implements Transformer {
    @Override
    public byte[] transform(ClassLoader loader, String name, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] bytes) {
        if (classBeingRedefined == minecraftClass) {
            ClassReader classReader = new ClassReader(bytes);
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, 0);
            for (MethodNode method : (List<MethodNode>) classNode.methods) {
                if (method.name.equals(runGameLoopMethod.getName()) && method.desc.equals("()V")) {
                    for (AbstractInsnNode abstractInsnNode : method.instructions.toArray()) {
                        if (abstractInsnNode instanceof MethodInsnNode) {
                            MethodInsnNode methodInsnNode = (MethodInsnNode) abstractInsnNode;

                            if (methodInsnNode.getOpcode() == INVOKEVIRTUAL
                                    && methodInsnNode.owner.equals(entityRendererClass.getName().replace('.', '/'))
                                    && methodInsnNode.name.equals(updateCameraAndRenderMethod.getName())
                                    && methodInsnNode.desc.equals("(FJ)V")) {
                                method.instructions.insert(methodInsnNode, onRender());

                                break;
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

    private InsnList onRender() {
        InsnList insnList = new InsnList();

        insnList.add(new FieldInsnNode(GETSTATIC, "com/shtruz/externalfinalscounter/ExternalFinalsCounter", "instance", "Lcom/shtruz/externalfinalscounter/ExternalFinalsCounter;"));
        insnList.add(new MethodInsnNode(INVOKEVIRTUAL, "com/shtruz/externalfinalscounter/ExternalFinalsCounter", "onRender", "()V", false));

        return insnList;
    }
}
