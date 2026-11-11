package com.openblacklist;

import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Patches com.mojang.patchy.BlockedServers so every server is reported as not blocked.
 * isBlockedServer(String) and isBlockedServerHostName(String) both become "return false".
 */
public class BlacklistTransformer implements IClassTransformer, Opcodes {
    private static final Logger LOGGER = LogManager.getLogger("OpenBlacklist/ASM");
    private static final String TARGET_CLASS = "com.mojang.patchy.BlockedServers";
    private static final String DESC = "(Ljava/lang/String;)Z";
    public static final List<String> PATCHED = new ArrayList<String>();

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (basicClass == null || !TARGET_CLASS.equals(transformedName)) return basicClass;

        LOGGER.info("Transforming {}", TARGET_CLASS);
        ClassNode cn = new ClassNode();
        new ClassReader(basicClass).accept(cn, 0);

        for (MethodNode mn : cn.methods) {
            boolean target = mn.desc.equals(DESC)
                    && (mn.name.equals("isBlockedServer") || mn.name.equals("isBlockedServerHostName"));
            if (!target) continue;

            mn.instructions.clear();
            mn.tryCatchBlocks.clear();
            if (mn.localVariables != null) mn.localVariables.clear();
            mn.instructions.add(new InsnNode(ICONST_0));
            mn.instructions.add(new InsnNode(IRETURN));
            PATCHED.add(TARGET_CLASS + "." + mn.name + mn.desc);
            LOGGER.info("Patched {}.{}{} -> return false", TARGET_CLASS, mn.name, mn.desc);
        }

        if (PATCHED.isEmpty()) {
            LOGGER.warn("Found {} but no target method matched.", TARGET_CLASS);
            return basicClass;
        }
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cn.accept(cw);
        return cw.toByteArray();
    }
}
