/*
 * JBoss, Community-driven Open Source Middleware
 * Copyright 2010, JBoss by Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.seam.faces.event;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.inject.Inject;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.faces.MockLogger;
import org.jboss.seam.faces.cdi.BeanManagerAware;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.Archives;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.impl.base.asset.ByteArrayAsset;
import org.jboss.test.faces.mock.context.MockFacesContext;
import org.jboss.test.faces.mock.lifecycle.MockLifecycle;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author Nicklas Karlsson
 *
 */
@RunWith(Arquillian.class)
public class PhaseEventBridgeTest
{
   private static List<PhaseId> ALL_PHASES = new ArrayList<PhaseId>()
   {
      private static final long serialVersionUID = 1L;

      {
         add(PhaseId.APPLY_REQUEST_VALUES);
         add(PhaseId.INVOKE_APPLICATION);
         add(PhaseId.PROCESS_VALIDATIONS);
         add(PhaseId.RENDER_RESPONSE);
         add(PhaseId.RESTORE_VIEW);
         add(PhaseId.UPDATE_MODEL_VALUES);
      }
   };

   @Deployment
   public static JavaArchive createTestArchive()
   {
      return Archives.create("test.jar", JavaArchive.class).addClasses(PhaseEventObserver.class, PhaseEventBridge.class, BeanManagerAware.class, MockLogger.class).addManifestResource(new ByteArrayAsset(new byte[0]), ArchivePaths.create("beans.xml"));
   }

   @Inject
   PhaseEventBridge phaseEventBridge;
   @Inject
   PhaseEventObserver observer;

   private final MockFacesContext facesContext = new MockFacesContext();
   private final MockLifecycle lifecycle = new MockLifecycle();

   private void fireAllPhases()
   {
      fireAllBeforePhases();
      fireAllAfterPhases();
   }

   private void fireAllBeforePhases()
   {
      fireBeforePhases(ALL_PHASES);
   }

   private void fireBeforePhases(List<PhaseId> phases)
   {
      for (PhaseId phaseId : phases)
      {
         fireBeforePhase(phaseId);
      }
   }

   private void fireBeforePhase(PhaseId phaseId)
   {
      phaseEventBridge.beforePhase(new PhaseEvent(facesContext, phaseId, lifecycle));
   }

   private void fireAllAfterPhases()
   {
      fireAfterPhases(ALL_PHASES);
   }

   private void fireAfterPhases(List<PhaseId> phases)
   {
      for (PhaseId phaseId : phases)
      {
         fireAfterPhase(phaseId);
      }
   }

   private void fireAfterPhase(PhaseId phaseId)
   {
      phaseEventBridge.afterPhase(new PhaseEvent(facesContext, phaseId, lifecycle));
   }

   @Test
   public void testBeforeRenderResponseObserver()
   {
      observer.reset();
      fireAllPhases();
      observer.assertObservations("1", PhaseId.RENDER_RESPONSE);
   }

   @Test
   public void testAfterRenderResponseObserver()
   {
      observer.reset();
      fireAllPhases();
      observer.assertObservations("2", PhaseId.RENDER_RESPONSE);
   }

   @Test
   public void testBeforeApplyRequestValuesObserver()
   {
      observer.reset();
      fireAllPhases();
      observer.assertObservations("3", PhaseId.APPLY_REQUEST_VALUES);
   }

   @Test
   public void testAfterApplyRequestValuesObserver()
   {
      observer.reset();
      fireAllPhases();
      observer.assertObservations("4", PhaseId.APPLY_REQUEST_VALUES);
   }

   @Test
   public void testBeforeInvokeApplicationObserver()
   {
      observer.reset();
      fireAllPhases();
      observer.assertObservations("5", PhaseId.INVOKE_APPLICATION);
   }

   @Test
   public void testAfterInvokeApplicationObserver()
   {
      observer.reset();
      fireAllPhases();
      observer.assertObservations("6", PhaseId.INVOKE_APPLICATION);
   }

   @Test
   public void testBeforeProcessValidationsObserver()
   {
      observer.reset();
      fireAllPhases();
      observer.assertObservations("7", PhaseId.PROCESS_VALIDATIONS);
   }

   @Test
   public void testAfterProcessValidationsObserver()
   {
      observer.reset();
      fireAllPhases();
      observer.assertObservations("8", PhaseId.PROCESS_VALIDATIONS);
   }

   @Test
   public void testBeforeRestoreViewObserver()
   {
      observer.reset();
      fireAllPhases();
      observer.assertObservations("9", PhaseId.RESTORE_VIEW);
   }

   @Test
   public void testAfterRestoreViewObserver()
   {
      observer.reset();
      fireAllPhases();
      observer.assertObservations("10", PhaseId.RESTORE_VIEW);
   }

   @Test
   public void testBeforeUpdateModelValuesObserver()
   {
      observer.reset();
      fireAllPhases();
      observer.assertObservations("11", PhaseId.UPDATE_MODEL_VALUES);
   }

   @Test
   public void testAfterUpdateModelValuesObserver()
   {
      observer.reset();
      fireAllPhases();
      observer.assertObservations("11", PhaseId.UPDATE_MODEL_VALUES);
   }

   @Test
   public void testAllRenderResponseObserver()
   {
      observer.reset();
      fireAllPhases();
      observer.assertObservations("13", PhaseId.RENDER_RESPONSE, PhaseId.RENDER_RESPONSE);
   }

   @Test
   public void testAllApplyRequestValuesObserver()
   {
      observer.reset();
      fireAllPhases();
      observer.assertObservations("14", PhaseId.APPLY_REQUEST_VALUES, PhaseId.APPLY_REQUEST_VALUES);
   }

   @Test
   public void testAllInvokeApplicationObserver()
   {
      observer.reset();
      fireAllPhases();
      observer.assertObservations("15", PhaseId.INVOKE_APPLICATION, PhaseId.INVOKE_APPLICATION);
   }

   @Test
   public void testAllProcessValidationsObserver()
   {
      observer.reset();
      fireAllPhases();
      observer.assertObservations("16", PhaseId.PROCESS_VALIDATIONS, PhaseId.PROCESS_VALIDATIONS);
   }

   @Test
   public void testAllRestoreViewObserver()
   {
      observer.reset();
      fireAllPhases();
      observer.assertObservations("17", PhaseId.RESTORE_VIEW, PhaseId.RESTORE_VIEW);
   }

   @Test
   public void testAllUpdateModelValuesObserver()
   {
      observer.reset();
      fireAllPhases();
      observer.assertObservations("18", PhaseId.UPDATE_MODEL_VALUES, PhaseId.UPDATE_MODEL_VALUES);
   }

   @Test
   public void testAllBeforeEventsObserver()
   {
      observer.reset();
      fireAllPhases();
      observer.assertObservations("19", PhaseId.APPLY_REQUEST_VALUES, PhaseId.INVOKE_APPLICATION, PhaseId.PROCESS_VALIDATIONS, PhaseId.RENDER_RESPONSE, PhaseId.RESTORE_VIEW, PhaseId.UPDATE_MODEL_VALUES);
   }

   @Test
   public void testAllAfterEventsObserver()
   {
      observer.reset();
      fireAllPhases();
      observer.assertObservations("20", PhaseId.APPLY_REQUEST_VALUES, PhaseId.INVOKE_APPLICATION, PhaseId.PROCESS_VALIDATIONS, PhaseId.RENDER_RESPONSE, PhaseId.RESTORE_VIEW, PhaseId.UPDATE_MODEL_VALUES);
   }

   @Test
   public void testAllEventsObserver()
   {
      observer.reset();
      fireAllPhases();
      observer.assertObservations("21", PhaseId.APPLY_REQUEST_VALUES, PhaseId.INVOKE_APPLICATION, PhaseId.PROCESS_VALIDATIONS, PhaseId.RENDER_RESPONSE, PhaseId.RESTORE_VIEW, PhaseId.UPDATE_MODEL_VALUES, PhaseId.APPLY_REQUEST_VALUES, PhaseId.INVOKE_APPLICATION, PhaseId.PROCESS_VALIDATIONS, PhaseId.RENDER_RESPONSE, PhaseId.RESTORE_VIEW, PhaseId.UPDATE_MODEL_VALUES);
   }

}