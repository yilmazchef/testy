package be.intecbrussel.student.views.commons;

import be.intecbrussel.student.util.BarcodeGenerator;
import be.intecbrussel.student.views.AbstractView;
import be.intecbrussel.student.views.MainAppLayout;
import com.flowingcode.vaadin.addons.simpletimer.SimpleTimer;
import com.github.appreciated.app.layout.addons.notification.entity.DefaultNotification;
import com.github.appreciated.app.layout.addons.notification.entity.Priority;
import com.google.zxing.WriterException;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

@PageTitle(ContinueOnAnotherDeviceView.TITLE)
@Route(value = ContinueOnAnotherDeviceView.ROUTE, layout = MainAppLayout.class)
public class ContinueOnAnotherDeviceView extends AbstractView {

    public static final String TITLE = "Continue with QR";
    public static final String ROUTE = "continue_qr";
    private final VaadinSession currentSession = UI.getCurrent().getSession();

    private final MainAppLayout appLayout;
    private final BarcodeGenerator barcodeGenerator;

    public ContinueOnAnotherDeviceView(MainAppLayout appLayout, BarcodeGenerator barcodeGenerator) {
        this.appLayout = appLayout;
        this.barcodeGenerator = barcodeGenerator;

        final var viewId = ROUTE + "_" + currentSession.getSession().getId();
        this.setId(viewId);

        initStyle();

        UI.getCurrent().getPage().executeJs("return window.location.href").then(String.class, absoluteUrl -> {

            try {

                final var requestLabel = new Label("If you do NOT scan the barcode, your request will be automatically cancelled. You will be redirected back where you left");
                final var requestTimer = new SimpleTimer();
                requestTimer.getStyle().set("font-size", "24pt");
                requestTimer.setStartTime(120);
                requestTimer.setMinutes(true);
                requestTimer.setFractions(false);
                requestTimer.addTimerEndEvent(onTimeEvent -> cancelRequestAndContinue(absoluteUrl));
                requestTimer.addCurrentTimeChangeListener(onTimeChange -> {

                }, 120, TimeUnit.SECONDS);
                requestTimer.start();

                final var qrCodeBytes = barcodeGenerator.toQRCode(MessageFormat.format("{0}?sessionId={1}", absoluteUrl, currentSession.getSession().getId()), 350, 350);
                final var qrCodeStream = new StreamResource("testy_qr.png", () -> new ByteArrayInputStream(qrCodeBytes));
                final var qrCodeImage = new Image(qrCodeStream, "QR CODE");

                final var cancelButton = new Button("Cancel Request & Continue Here", onClick -> cancelRequestAndContinue(absoluteUrl));
                cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

                add(requestLabel, requestTimer, qrCodeImage, cancelButton);

            } catch (WriterException writerException) {
                final var message = "Could not generate QR Code, WriterException :: " + writerException.getMessage();
                appLayout.getNotifications().add(new DefaultNotification("Error !!", message, Priority.ERROR));
            } catch (IOException ioException) {
                final var message = "Could not generate QR Code, IOException :: " + ioException.getMessage();
                appLayout.getNotifications().add(new DefaultNotification("Error !!", message, Priority.ERROR));
            }
        });

    }

    private void cancelRequestAndContinue(String absoluteUrl) {
        final var recentRoute = absoluteUrl.replace(ContinueOnAnotherDeviceView.ROUTE, "");
        UI.getCurrent().navigate(recentRoute);
    }

    private void initStyle() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }

    @Override
    public String getViewName() {
        return getClass().getName();
    }
}