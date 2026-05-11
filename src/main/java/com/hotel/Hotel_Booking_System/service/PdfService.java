package com.hotel.Hotel_Booking_System.service;

import com.hotel.Hotel_Booking_System.model.Booking;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    public byte[] generateBookingReceipt(Booking booking) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();

            // ── Fonts ──
            Font titleFont   = new Font(Font.HELVETICA, 22, Font.BOLD,   new Color(13, 110, 253));
            Font headingFont = new Font(Font.HELVETICA, 13, Font.BOLD,   new Color(33, 37, 41));
            Font labelFont   = new Font(Font.HELVETICA, 11, Font.BOLD,   new Color(80, 80, 80));
            Font valueFont   = new Font(Font.HELVETICA, 11, Font.NORMAL, new Color(33, 37, 41));
            Font smallFont   = new Font(Font.HELVETICA,  9, Font.NORMAL, new Color(120, 120, 120));
            Font greenFont   = new Font(Font.HELVETICA, 13, Font.BOLD,   new Color(25, 135, 84));

            // ── Header ──
            Paragraph brand = new Paragraph("Tripster", titleFont);
            brand.setAlignment(Element.ALIGN_CENTER);
            document.add(brand);

            Paragraph tagline = new Paragraph("Your Booking Receipt", smallFont);
            tagline.setAlignment(Element.ALIGN_CENTER);
            tagline.setSpacingAfter(10);
            document.add(tagline);

            // ── Divider ──
            addDivider(document);

            // ── Booking Ref ──
            Paragraph ref = new Paragraph("Booking Reference: " + booking.getBookingRef(), headingFont);
            ref.setAlignment(Element.ALIGN_CENTER);
            ref.setSpacingBefore(10);
            ref.setSpacingAfter(5);
            document.add(ref);

            Paragraph status = new Paragraph("Status: " + booking.getStatus(), greenFont);
            status.setAlignment(Element.ALIGN_CENTER);
            status.setSpacingAfter(15);
            document.add(status);

            addDivider(document);

            // ── Details Table ──
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(90);
            table.setSpacingBefore(15);
            table.setSpacingAfter(15);
            table.setWidths(new float[]{35f, 65f});

            addRow(table, "Guest Name",   booking.getUser().getName(),           labelFont, valueFont);
            addRow(table, "Email",        booking.getUser().getEmail(),           labelFont, valueFont);
            addRow(table, "Hotel",        booking.getHotel().getName(),           labelFont, valueFont);
            addRow(table, "City",         booking.getHotel().getCity(),           labelFont, valueFont);
            addRow(table, "Room Type",    booking.getRoomType() != null
                                            ? booking.getRoomType() : "Standard", labelFont, valueFont);
            addRow(table, "Check-in",     booking.getCheckIn().toString(),        labelFont, valueFont);
            addRow(table, "Check-out",    booking.getCheckOut().toString(),       labelFont, valueFont);

            long days = java.time.temporal.ChronoUnit.DAYS.between(
                            booking.getCheckIn(), booking.getCheckOut());
            if (days == 0) days = 1;
            addRow(table, "Total Nights", String.valueOf(days),                   labelFont, valueFont);
            addRow(table, "Total Amount", "Rs. " + booking.getTotalPrice(),       labelFont, valueFont);

            document.add(table);

            addDivider(document);

            // ── Footer ──
            Paragraph footer = new Paragraph(
                "Thank you for booking with Tripster! Have a great stay.", smallFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(10);
            document.add(footer);

            Paragraph generated = new Paragraph(
                "Generated on: " + java.time.LocalDate.now(), smallFont);
            generated.setAlignment(Element.ALIGN_CENTER);
            document.add(generated);

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }

    // ── Helper: divider line ──
    private void addDivider(Document document) throws DocumentException {
        Paragraph line = new Paragraph("________________________________________________");
        line.setAlignment(Element.ALIGN_CENTER);
        Font grayFont = new Font(Font.HELVETICA, 10, Font.NORMAL, new Color(200, 200, 200));
        line.getFont().setColor(new Color(200, 200, 200));
        document.add(line);
    }

    // ── Helper: table row ──
    private void addRow(PdfPTable table, String label, String value,
                        Font labelFont, Font valueFont) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(6);
        labelCell.setBackgroundColor(new Color(248, 249, 250));

        PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(6);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }
}