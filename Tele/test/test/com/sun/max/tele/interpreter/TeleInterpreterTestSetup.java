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
package test.com.sun.max.tele.interpreter;

import junit.framework.*;
import test.com.sun.max.vm.cps.*;

import com.sun.max.tele.*;
import com.sun.max.tele.interpreter.*;
import com.sun.max.vm.*;
import com.sun.max.vm.actor.member.*;
import com.sun.max.vm.cps.ir.*;
import com.sun.max.vm.cps.ir.interpreter.*;
import com.sun.max.vm.hosted.*;

/**
 *  @author Athul Acharya
 */
public class TeleInterpreterTestSetup extends CompilerTestSetup<ActorIrMethod> {

    @Override
    protected IrInterpreter<ActorIrMethod> createInterpreter() {
        return new TeleInterpreter(null);
    }

    public TeleInterpreterTestSetup(Test test, TeleVM teleVM) {
        super(test);
    }

    public TeleInterpreterTestSetup(Test test) {
        super(test);
    }

    @Override
    protected void initializeVM() {
        VMConfigurator vmConfigurator = new VMConfigurator(null);
        vmConfigurator.gripScheme.setValue(new com.sun.max.vm.grip.hosted.Package());
        vmConfigurator.referenceScheme.setValue(new com.sun.max.vm.reference.hosted.Package());
        vmConfigurator.heapScheme.setValue(new com.sun.max.vm.heap.hosted.Package());
        vmConfigurator.layoutScheme.setValue(new com.sun.max.vm.layout.hosted.Package());
        vmConfigurator.monitorScheme.setValue(new com.sun.max.vm.monitor.hosted.Package());
        vmConfigurator.buildLevel.setValue(BuildLevel.DEBUG);
        vmConfigurator.create(true);
    }

    @Override
    public ActorIrMethod translate(ClassMethodActor classMethodActor) {
        return new ActorIrMethod(classMethodActor);
    }

}
