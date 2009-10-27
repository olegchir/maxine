/*
 * Copyright (c) 2007 Sun Microsystems, Inc.  All rights reserved.
 *
 * Sun Microsystems, Inc. has intellectual property rights relating to technology embodied in the product
 * that is described in this document. In particular, and without limitation, these intellectual property
 * rights may include one or more of the U.S. patents listed at http://www.sun.com/patents and one or
 * more additional patents or pending patent applications in the U.S. and in other countries.
 *
 * U.S. Government Rights - Commercial software. Government users are subject to the Sun
 * Microsystems, Inc. standard license agreement and applicable provisions of the FAR and its
 * supplements.
 *
 * Use is subject to license terms. Sun, Sun Microsystems, the Sun logo, Java and Solaris are trademarks or
 * registered trademarks of Sun Microsystems, Inc. in the U.S. and other countries. All SPARC trademarks
 * are used under license and are trademarks or registered trademarks of SPARC International, Inc. in the
 * U.S. and other countries.
 *
 * UNIX is a registered trademark in the U.S. and other countries, exclusively licensed through X/Open
 * Company, Ltd.
 */
package com.sun.max.vm.stack;

import com.sun.max.unsafe.*;
import com.sun.max.vm.compiler.target.*;

/**
 * A visitor for traversing the frames on a thread's stack. The details of each frame traversed in the
 * stack walk are passed to {@link #visitFrame(TargetMethod, Pointer, Pointer, Pointer, int)}
 * in a newly allocated {@link StackFrame} object.
 *
 * @see RawStackFrameVisitor
 * @author Doug Simon
 */
public interface StackFrameVisitor {

    /**
     * Processes a given frame that is being traversed as part of a {@linkplain StackFrameWalker#walk stack walk}.
     *
     * @param stackFrame an object encapsulating the details of the frame
     * @return true if the walk should continue to the caller of {@code stackFrame}, false if it should terminate now
     */
    boolean visitFrame(StackFrame stackFrame);
}
