//package com.sinergia.dcargo.client.local.view;
//
//import com.google.gwt.cell.client.AbstractInputCell;
//import com.google.gwt.cell.client.ValueUpdater;
//import com.google.gwt.core.client.GWT;
//import com.google.gwt.dom.client.Element;
//import com.google.gwt.dom.client.InputElement;
//import com.google.gwt.dom.client.NativeEvent;
//import com.google.gwt.safecss.shared.SafeStyles;
//import com.google.gwt.safecss.shared.SafeStylesUtils;
//import com.google.gwt.safehtml.client.SafeHtmlTemplates;
//import com.google.gwt.safehtml.shared.SafeHtml;
//import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
//import com.google.gwt.safehtml.shared.SimpleHtmlSanitizer;
//import com.sinergia.dcargo.client.shared.Item;
//
///**
// * An input cell that changes color based on the validation status.
// */
//class ValidatableTotalInputCell extends AbstractInputCell<String, Item> {
//
//	private SafeHtml errorMessage;
//
//	private Template template;
//
//	private SafeHtmlBuilder sb;
//
//	// private
//
//	interface Template extends SafeHtmlTemplates {
//		@Template("<input type=\"number\" value=\"{0}\" style=\"{1}\" tabindex=\"-1\"/>")
//		SafeHtml input(String value, SafeStyles color);
//	}
//
//	public ValidatableTotalInputCell(String errorMessage) {
//		super("change");
//		if (template == null) {
//			template = GWT.create(Template.class);
//		}
//		this.errorMessage = SimpleHtmlSanitizer.sanitizeHtml(errorMessage);
//	}
//
//	@Override
//	public void onBrowserEvent(Context context, Element parent, String value, NativeEvent event,
//			ValueUpdater<String> valueUpdater) {
//		super.onBrowserEvent(context, parent, value, event, valueUpdater);
//
//		GWT.log("onBrowserEvent: value: " + value);
//
//		// Ignore events that don't target the input.
//		Element target = event.getEventTarget().cast();
//		if (!parent.getFirstChildElement().isOrHasChild(target)) {
//			return;
//		}
//
//		Object key = context.getKey();
//		Item viewData = getViewData(key);
//		String eventType = event.getType();
//		if ("change".equals(eventType)) {
//			InputElement input = parent.getFirstChild().cast();
//
//			// Mark cell as containing a pending change
//			input.getStyle().setColor("blue");
//
//			// Save the new value in the view data.
//			if (viewData == null) {
//				viewData = new Item();
//				setViewData(key, viewData);
//			}
//			String newValue = input.getValue();
//			viewData.setTotal(newValue.equals("") ? 0.0D : Double.valueOf(newValue));
//			finishEditing(parent, newValue, key, valueUpdater);
//
//			// Update the value updater, which updates the field updater.
//			if (valueUpdater != null) {
//				valueUpdater.update(newValue);
//			}
//		}
//
//		render(context, value, sb);
//	}
//
//	@Override
//	public void render(Context context, String value, SafeHtmlBuilder sb) {
//
//		this.sb = sb;
//
//		GWT.log("render() value: " + value);
//
//		// Get the view data.
//		Object key = context.getKey();
//		GWT.log("render() key: " + key);
//		Item viewData = getViewData(key);
//		if (viewData != null && viewData.getTotal().equals(value)) {
//			// Clear the view data if the value is the same as the current
//			// value.
//			clearViewData(key);
//			viewData = null;
//		}
//
//		GWT.log("render() viewData: " + viewData);
//		if (viewData != null) {
//			Double precio = viewData.getPrecio() == null ? 0.0D : viewData.getPrecio().getPrecio();
//			GWT.log("render() precio: " + precio);
//			Double peso = viewData.getPeso();
//			GWT.log("render() peso: " + peso);
//			Double total = precio * peso;
//			GWT.log("render() total: " + total);
//			viewData.setTotal(total);
//		}
//
//		/*
//		 * If viewData is null, just paint the contents black. If it is
//		 * non-null, show the pending value and paint the contents red if they
//		 * are known to be invalid.
//		 */
//		String pendingValue = (viewData == null) ? null : viewData.getTotal() + "";
//		boolean invalid = (viewData == null) ? false : viewData.getValido();
//
//		String color = pendingValue != null ? (invalid ? "red" : "blue") : "black";
//		SafeStyles safeColor = SafeStylesUtils.fromTrustedString("color: " + color + ";");
//		sb.append(template.input(pendingValue != null ? pendingValue : value, safeColor));
//
//		if (invalid) {
//			sb.appendHtmlConstant(" <span style='color:red;'>");
//			sb.append(errorMessage);
//			sb.appendHtmlConstant("</span>");
//		}
//
//	}
//
//	@Override
//	protected void onEnterKeyDown(Context context, Element parent, String value, NativeEvent event,
//			ValueUpdater<String> valueUpdater) {
//		Element target = event.getEventTarget().cast();
//		if (getInputElement(parent).isOrHasChild(target)) {
//			finishEditing(parent, value, context.getKey(), valueUpdater);
//		} else {
//			super.onEnterKeyDown(context, parent, value, event, valueUpdater);
//		}
//	}
//}