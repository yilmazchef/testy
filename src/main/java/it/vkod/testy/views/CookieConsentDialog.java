package it.vkod.testy.views;

import it.vkod.testy.data.http.CookieConsent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.util.Arrays;
import java.util.stream.Collectors;

import static it.vkod.testy.data.http.CookieConsent.*;
import static java.text.MessageFormat.format;

@AnonymousAllowed
public class CookieConsentDialog extends Dialog {

    public CookieConsentDialog() {

        final var session = VaadinSession.getCurrent();
        final var necessary = Necessary;

        final var layout = new VerticalLayout();

        final var isMandatory = (SerializablePredicate<CookieConsent>) consent -> consent != Necessary;
        final var isOptional = (SerializablePredicate<CookieConsent>) consent -> consent != Necessary;

        final var header = new H3(necessary.getHeader());
        final var body = new Paragraph(necessary.getBody());
        final var details = new Grid<CookieConsent>();
        details.setItems(Arrays.stream(values()));
        details.select(Necessary);
        details.setSelectionMode(Grid.SelectionMode.MULTI);
        details.addThemeVariants(GridVariant.LUMO_COMPACT);
        details.addComponentColumn(consent ->
                new Details(consent.name(), new Paragraph(consent.getDescription())))
                .setHeader("Description")
                .setAutoWidth(true);

        final var submit = new Button("Submit My Choices", onSubmit -> {
            final var selectedConsents = details.getSelectedItems();
            if (selectedConsents.contains(Necessary)) {
                session.setAttribute(CookieConsent[].class, selectedConsents.toArray(CookieConsent[]::new));
                close();
                final var cookieSettings = Arrays
                        .stream(session.getAttribute(CookieConsent[].class)).map(CookieConsent::name)
                        .collect(Collectors.toUnmodifiableList());
                notify(format("Cookie Consent choices are set to: {0}", String.join(", ", cookieSettings)));
            } else {
                notify(format("You do not have selected mandatory consents. " +
                        "Therefore, the default settings are applied: {0} and {1}", Necessary, Preferences));
            }
        });

        final var reject = new Button("Reject All Cookies", onReject -> {
            session.getSession().invalidate();
            close();
        });

        setCloseOnEsc(true);
        setWidthFull();
        setCloseOnOutsideClick(true);
        setDraggable(false);
        setModal(true);
        setResizable(false);
        setOpened(true);

        addDialogCloseActionListener(onClose -> notify(format("You have closed the cookie consent dialog. " +
                "Therefore, the default settings are applied: {0} and {1}", Necessary, Preferences)));

        layout.add(header, body, details, reject, submit);
        add(layout);
    }

    private void notify(final String message) {
        new Notification(message, 4000, Notification.Position.BOTTOM_CENTER).open();
    }
}
