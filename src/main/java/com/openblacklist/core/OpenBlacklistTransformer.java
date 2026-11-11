package com.openblacklist.core;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;

public class OpenBlacklistTransformer implements IClassTransformer {

    private static final String BLOCKED_SERVERS =
        "com.mojang.patchy.BlockedServers";

    private static final String BOOTSTRAP =
        "io.netty.bootstrap.Bootstrap";

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {

        if (basicClass == null) {
            return null;
        }

        String className = transformedName != null
            ? transformedName
            : name;

        if (className == null) {
            return basicClass;
        }

        className = className.replace('/', '.');

        if (!BLOCKED_SERVERS.equals(className)
            && !BOOTSTRAP.equals(className)) {
            return basicClass;
        }

        try {
            ClassReader reader = new ClassReader(basicClass);
            ClassNode classNode = new ClassNode();

            reader.accept(classNode, 0);

            boolean found = false;

            for (MethodNode method : classNode.methods) {

                if (BLOCKED_SERVERS.equals(className)
                    && method.name.equals("isBlockedServerHostName")
                    && method.desc.equals("(Ljava/lang/String;)Z")) {

                    method.instructions.clear();
                    method.instructions.add(
                        new InsnNode(Opcodes.ICONST_0)
                    );
                    method.instructions.add(
                        new InsnNode(Opcodes.IRETURN)
                    );

                    method.tryCatchBlocks.clear();
                    method.localVariables = null;
                    method.maxStack = 1;

                    System.out.println(
                        "[OpenBlacklist] Disabled Patchy hostname blacklist."
                    );

                    found = true;
                    break;
                }

                if (BOOTSTRAP.equals(className)
                    && method.name.equals("checkAddress")
                    && method.desc.equals(
                        "(Ljava/net/SocketAddress;)Lio/netty/channel/ChannelFuture;"
                    )) {

                    method.instructions.clear();
                    method.instructions.add(
                        new InsnNode(Opcodes.ACONST_NULL)
                    );
                    method.instructions.add(
                        new InsnNode(Opcodes.ARETURN)
                    );

                    method.tryCatchBlocks.clear();
                    method.localVariables = null;
                    method.maxStack = 1;

                    System.out.println(
                        "[OpenBlacklist] Disabled Netty blacklist check."
                    );

                    found = true;
                    break;
                }
            }

            if (!found) {
                System.err.println(
                    "[OpenBlacklist] ERROR: Target method not found in "
                    + className
                );

                return basicClass;
            }

            ClassWriter writer = new ClassWriter(
                ClassWriter.COMPUTE_MAXS
            );

            classNode.accept(writer);

            return writer.toByteArray();

        } catch (Throwable error) {
            System.err.println(
                "[OpenBlacklist] Transformation failed: " + className
            );

            error.printStackTrace();

            return basicClass;
        }
    }
}