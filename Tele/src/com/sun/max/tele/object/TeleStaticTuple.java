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
package com.sun.max.tele.object;

import java.util.*;

import com.sun.max.tele.*;
import com.sun.max.vm.actor.holder.*;
import com.sun.max.vm.actor.member.*;
import com.sun.max.vm.reference.*;

public class TeleStaticTuple extends TeleTupleObject {

    protected TeleStaticTuple(TeleVM teleVM, Reference reference) {
        super(teleVM, reference);
    }

    @Override
    public Set<FieldActor> getFieldActors() {
        final Set<FieldActor> staticFieldActors = new HashSet<FieldActor>();
        collectStaticFieldActors(classActorForType(), staticFieldActors);
        return staticFieldActors;
    }

    private void collectStaticFieldActors(ClassActor classActor, Set<FieldActor> staticFieldActors) {
        if (classActor != null) {
            for (FieldActor fieldActor : classActor.localStaticFieldActors()) {
                staticFieldActors.add(fieldActor);
            }
            collectStaticFieldActors(classActor.superClassActor(), staticFieldActors);
        }
    }

    @Override
    public String maxineRole() {
        return "StaticTuple";
    }

    @Override
    public TeleClassMethodActor getTeleClassMethodActorForObject() {
        // Some tuples might be some IR object associated with a method, but not static tuples.
        return null;
    }

    @Override
    public Object shallowCopy() {
        return StaticTuple.create(classActorForType());
    }

    @Override
    protected Object createDeepCopy(DeepCopyContext context) {
        return classActorForType().staticTuple();
    }

}
