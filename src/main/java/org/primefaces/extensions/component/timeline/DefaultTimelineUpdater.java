/*
 * Copyright 2011-2013 PrimeFaces Extensions.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Id: $
 */

package org.primefaces.extensions.component.timeline;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.primefaces.context.RequestContext;
import org.primefaces.extensions.model.timeline.TimelineEvent;
import org.primefaces.extensions.util.ComponentUtils;

/**
 * Default implementation of the {@link TimelineUpdater}.
 *
 * @author  Oleg Varaksin / last modified by $Author: $
 * @version $Revision: 1.0 $
 * @since   0.7
 */
public class DefaultTimelineUpdater extends TimelineUpdater implements PhaseListener {

	private static final long serialVersionUID = 20130317L;

	private static final Logger LOG = Logger.getLogger(DefaultTimelineUpdater.class.getName());

	private String widgetVar;
	private List<CrudOperationData> crudOperationDatas;

	@Override
	public void add(TimelineEvent event) {
		if (event == null) {
			return;
		}

		checkCrudOperationDataList();
		crudOperationDatas.add(new CrudOperationData(CrudOperation.ADD, event));
	}

	@Override
	public void update(TimelineEvent event, int index) {
		if (event == null) {
			return;
		}

		checkCrudOperationDataList();
		crudOperationDatas.add(new CrudOperationData(CrudOperation.UPDATE, event, index));
	}

	@Override
	public void delete(int index) {
		checkCrudOperationDataList();
		crudOperationDatas.add(new CrudOperationData(CrudOperation.DELETE, index));
	}

	@Override
	public void deleteAll(List<Integer> indexes) {
		checkCrudOperationDataList();
		crudOperationDatas.add(new CrudOperationData(CrudOperation.DELETE_ALL, indexes));
	}

	@Override
	public void clear() {
		checkCrudOperationDataList();
		crudOperationDatas.add(new CrudOperationData(CrudOperation.CLEAR));
	}

	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}

	public void beforePhase(PhaseEvent event) {
		if (crudOperationDatas == null) {
			return;
		}

		FacesContext fc = event.getFacesContext();
		StringBuilder sb = new StringBuilder();

		Timeline timeline = (Timeline) fc.getViewRoot().findComponent(id);
		TimelineRenderer timelineRenderer =
		    (TimelineRenderer) fc.getRenderKit().getRenderer(Timeline.COMPONENT_FAMILY, Timeline.DEFAULT_RENDERER);
		Calendar calendar = Calendar.getInstance(ComponentUtils.resolveTimeZone(timeline.getTimeZone()));

		try {
			for (CrudOperationData crudOperationData : crudOperationDatas) {
				switch (crudOperationData.getCrudOperation()) {
				case ADD:

					sb.append(widgetVar);
					sb.append(".addItem(");
					sb.append(timelineRenderer.encodeEvent(fc, timeline, calendar, crudOperationData.getEvent()));
					sb.append(");");
					break;

				case UPDATE:

					sb.append(widgetVar);
					sb.append(".changeItem(");
					sb.append(crudOperationData.getIndex());
					sb.append(",");
					sb.append(timelineRenderer.encodeEvent(fc, timeline, calendar, crudOperationData.getEvent()));
					sb.append(");");
					break;

				case DELETE:

					sb.append(widgetVar);
					sb.append(".deleteItem(");
					sb.append(crudOperationData.getIndex());
					sb.append(",false);");
					break;

				case DELETE_ALL:

					List<Integer> indexes = crudOperationData.getIndexes();
					int size = indexes != null ? indexes.size() : 0;
					if (size == 0) {
						break;
					}

					for (int i = 0; i < size; i++) {
						sb.append(widgetVar);
						sb.append(".deleteItem(");
						sb.append(crudOperationData.getIndex());
						if (i + 1 < size) {
							sb.append(",true);");
						} else {
							sb.append(",false);");
						}
					}

					break;

				case CLEAR:

					sb.append(widgetVar);
					sb.append(".deleteAllItems();");
					break;
				}
			}

			// execute JS script
			RequestContext.getCurrentInstance().execute(sb.toString());
		} catch (IOException e) {
			LOG.log(Level.WARNING, "Timeline with id " + id + " could not be updated, at least one CRUD operation failed", e);
		}
	}

	public void afterPhase(PhaseEvent event) {
		// NOOP.
	}

	public void setWidgetVar(String widgetVar) {
		this.widgetVar = widgetVar;
	}

	private void checkCrudOperationDataList() {
		if (crudOperationDatas == null) {
			crudOperationDatas = new ArrayList<CrudOperationData>();
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		DefaultTimelineUpdater that = (DefaultTimelineUpdater) o;

		if (widgetVar != null ? !widgetVar.equals(that.widgetVar) : that.widgetVar != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return widgetVar != null ? widgetVar.hashCode() : 0;
	}

	class CrudOperationData implements Serializable {

		private CrudOperation crudOperation;
		private TimelineEvent event;
		private int index;
		private List<Integer> indexes;

		CrudOperationData(CrudOperation crudOperation) {
			this.crudOperation = crudOperation;
		}

		CrudOperationData(CrudOperation crudOperation, TimelineEvent event) {
			this.crudOperation = crudOperation;
			this.event = event;
		}

		CrudOperationData(CrudOperation crudOperation, int index) {
			this.crudOperation = crudOperation;
			this.index = index;
		}

		CrudOperationData(CrudOperation crudOperation, List<Integer> indexes) {
			this.crudOperation = crudOperation;
			this.indexes = indexes;
		}

		CrudOperationData(CrudOperation crudOperation, TimelineEvent event, int index) {
			this.crudOperation = crudOperation;
			this.event = event;
			this.index = index;
		}

		public CrudOperation getCrudOperation() {
			return crudOperation;
		}

		public TimelineEvent getEvent() {
			return event;
		}

		public int getIndex() {
			return index;
		}

		public List<Integer> getIndexes() {
			return indexes;
		}
	}

	/**
	 * CrudOperation
	 *
	 * @author  Oleg Varaksin / last modified by $Author: $
	 * @version $Revision: 1.0 $
	 */
	enum CrudOperation {

		ADD,
		UPDATE,
		DELETE,
		DELETE_ALL,
		CLEAR
	}
}
