package org.careconnect.careconnectbooking.service.serviceImpl;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class JSONToPDF {

    public byte[] generatePDF(String jsonStr) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        createPDF(jsonStr, baos);
        return baos.toByteArray();
    }

    private void createPDF(String jsonStr, ByteArrayOutputStream baos) throws IOException {
        JSONObject jsonObject = new JSONObject(jsonStr);

        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);
        document.setMargins(20, 20, 20, 20);

        Style titleStyle = new Style();
        titleStyle.setFontSize(20);
        titleStyle.setBold();
        titleStyle.setFontColor(new DeviceRgb(0, 0, 128));
        titleStyle.setTextAlignment(TextAlignment.CENTER);

        Style headerStyle = new Style();
        headerStyle.setFontSize(14);
        headerStyle.setBold();
        headerStyle.setFontColor(new DeviceRgb(255, 255, 255));
        headerStyle.setBackgroundColor(new DeviceRgb(0, 0, 128));
        headerStyle.setTextAlignment(TextAlignment.CENTER);

        Style cellStyle = new Style();
        cellStyle.setFontSize(12);
        cellStyle.setTextAlignment(TextAlignment.CENTER);

        JSONArray dataArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject appointment = dataArray.getJSONObject(i);
            JSONObject doctor = appointment.getJSONObject("doctor");
            JSONObject patient = appointment.getJSONObject("patient");

            document.add(new Paragraph("Appointment Details").addStyle(titleStyle));

            document.add(new Paragraph("Doctor Information").addStyle(headerStyle));
            Table doctorTable = new Table(new float[]{1, 3});
            doctorTable.setWidth(PageSize.A4.getWidth() - document.getLeftMargin() - document.getRightMargin());
            doctorTable.addHeaderCell(new Cell().add(new Paragraph("Field").addStyle(headerStyle)));
            doctorTable.addHeaderCell(new Cell().add(new Paragraph("Value").addStyle(headerStyle)));

            addDoctorInfo(doctor, doctorTable, cellStyle);
            document.add(doctorTable);

            document.add(new Paragraph("Patient Information").addStyle(headerStyle));
            Table patientTable = new Table(new float[]{1, 3});
            patientTable.setWidth(PageSize.A4.getWidth() - document.getLeftMargin() - document.getRightMargin());
            patientTable.addHeaderCell(new Cell().add(new Paragraph("Field").addStyle(headerStyle)));
            patientTable.addHeaderCell(new Cell().add(new Paragraph("Value").addStyle(headerStyle)));

            addPatientInfo(patient, patientTable, cellStyle);
            document.add(patientTable);



            document.add(new Paragraph("Patient Illness").addStyle(headerStyle));
            Table illnessTable = new Table(new float[]{1, 3});
            illnessTable.setWidth(PageSize.A4.getWidth() - document.getLeftMargin() - document.getRightMargin());
            illnessTable.addHeaderCell(new Cell().add(new Paragraph("Field").addStyle(headerStyle)));
            illnessTable.addHeaderCell(new Cell().add(new Paragraph("Value").addStyle(headerStyle)));

            getIllness(patient, illnessTable, cellStyle);
            document.add(illnessTable);

            document.add(new Paragraph("Appointment Information").addStyle(headerStyle));
            Table appointmentTable = new Table(new float[]{1, 3});
            appointmentTable.setWidth(PageSize.A4.getWidth() - document.getLeftMargin() - document.getRightMargin());
            appointmentTable.addHeaderCell(new Cell().add(new Paragraph("Field").addStyle(headerStyle)));
            appointmentTable.addHeaderCell(new Cell().add(new Paragraph("Value").addStyle(headerStyle)));

            addAppointmentInfo(appointment, appointmentTable, cellStyle);
            document.add(appointmentTable);
        }

        document.close();
    }

    private static void addDoctorInfo(JSONObject doctor, Table table, Style cellStyle) {
        table.addCell(new Cell().add(new Paragraph("Doctor ID").addStyle(cellStyle)));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(doctor.getInt("doctorId"))).addStyle(cellStyle)));

        table.addCell(new Cell().add(new Paragraph("Name").addStyle(cellStyle)));
        String middleName = doctor.getJSONObject("name").isNull("middleName") ? "" : doctor.getJSONObject("name").getString("middleName");
        String doctorName = doctor.getJSONObject("name").getString("firstName") + " " + middleName + " " + doctor.getJSONObject("name").getString("lastName");
        table.addCell(new Cell().add(new Paragraph(doctorName).addStyle(cellStyle)));

        table.addCell(new Cell().add(new Paragraph("Email").addStyle(cellStyle)));
        table.addCell(new Cell().add(new Paragraph(doctor.getString("email")).addStyle(cellStyle)));

        table.addCell(new Cell().add(new Paragraph("Mobile No").addStyle(cellStyle)));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(doctor.getLong("mobileNo"))).addStyle(cellStyle)));

        table.addCell(new Cell().add(new Paragraph("Gender").addStyle(cellStyle)));
        table.addCell(new Cell().add(new Paragraph(doctor.getString("gender")).addStyle(cellStyle)));

        table.addCell(new Cell().add(new Paragraph("License Number").addStyle(cellStyle)));
        table.addCell(new Cell().add(new Paragraph(doctor.getString("licenseNumber")).addStyle(cellStyle)));

        table.addCell(new Cell().add(new Paragraph("Address").addStyle(cellStyle)));
        JSONObject address = doctor.getJSONObject("address");
        String doctorAddress = address.getString("street") + ", " + address.getString("city") + ", " + address.getString("state") + ", " + address.getString("postalCode") + ", " + address.getString("country");
        table.addCell(new Cell().add(new Paragraph(doctorAddress).addStyle(cellStyle)));

        table.addCell(new Cell().add(new Paragraph("Adhar No").addStyle(cellStyle)));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(doctor.getLong("adharNo"))).addStyle(cellStyle)));

        table.addCell(new Cell().add(new Paragraph("Blood Group").addStyle(cellStyle)));
        table.addCell(new Cell().add(new Paragraph(doctor.getString("bloodGroup")).addStyle(cellStyle)));

        table.addCell(new Cell().add(new Paragraph("Marital Status").addStyle(cellStyle)));
        table.addCell(new Cell().add(new Paragraph(doctor.getString("maritalStatus")).addStyle(cellStyle)));

        table.addCell(new Cell().add(new Paragraph("Specialization").addStyle(cellStyle)));
        table.addCell(new Cell().add(new Paragraph(doctor.getString("specialization")).addStyle(cellStyle)));
    }

    private static void addPatientInfo(JSONObject patient, Table table, Style cellStyle) {
        table.addCell(new Cell().add(new Paragraph("Patient ID").addStyle(cellStyle)));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(patient.getInt("patientId"))).addStyle(cellStyle)));

        table.addCell(new Cell().add(new Paragraph("Name").addStyle(cellStyle)));
        String middleName = patient.getJSONObject("name").isNull("middleName") ? "" : patient.getJSONObject("name").getString("middleName");
        String patientName = patient.getJSONObject("name").getString("firstName") + " " + middleName + " " + patient.getJSONObject("name").getString("lastName");
        table.addCell(new Cell().add(new Paragraph(patientName).addStyle(cellStyle)));

        table.addCell(new Cell().add(new Paragraph("Email").addStyle(cellStyle)));
        table.addCell(new Cell().add(new Paragraph(patient.getString("email")).addStyle(cellStyle)));

        table.addCell(new Cell().add(new Paragraph("Mobile No").addStyle(cellStyle)));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(patient.getLong("mobileNo"))).addStyle(cellStyle)));

        table.addCell(new Cell().add(new Paragraph("Gender").addStyle(cellStyle)));
        table.addCell(new Cell().add(new Paragraph(patient.getString("gender")).addStyle(cellStyle)));

        table.addCell(new Cell().add(new Paragraph("Birthdate").addStyle(cellStyle)));
        table.addCell(new Cell().add(new Paragraph(patient.getString("birthdate")).addStyle(cellStyle)));

        table.addCell(new Cell().add(new Paragraph("Address").addStyle(cellStyle)));
        JSONObject address = patient.getJSONObject("address");
        String patientAddress = address.getString("street") + ", " + address.getString("city") + ", " + address.getString("state") + ", " + address.getString("postalCode") + ", " + address.getString("country");
        table.addCell(new Cell().add(new Paragraph(patientAddress).addStyle(cellStyle)));

        table.addCell(new Cell().add(new Paragraph("Adhar No").addStyle(cellStyle)));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(patient.getLong("adharNo"))).addStyle(cellStyle)));

        table.addCell(new Cell().add(new Paragraph("Blood Group").addStyle(cellStyle)));
        table.addCell(new Cell().add(new Paragraph(patient.getString("bloodGroup")).addStyle(cellStyle)));

        table.addCell(new Cell().add(new Paragraph("Marital Status").addStyle(cellStyle)));
        table.addCell(new Cell().add(new Paragraph(patient.getString("maritalStatus")).addStyle(cellStyle)));

        table.addCell(new Cell().add(new Paragraph("Occupation").addStyle(cellStyle)));
        table.addCell(new Cell().add(new Paragraph(patient.getString("occupation")).addStyle(cellStyle)));

        table.addCell(new Cell().add(new Paragraph("Emergency Contact Number").addStyle(cellStyle)));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(patient.getLong("emergencyContactNumber"))).addStyle(cellStyle)));

        table.addCell(new Cell().add(new Paragraph("Is Smoker").addStyle(cellStyle)));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(patient.getBoolean("isSmoker"))).addStyle(cellStyle)));

        table.addCell(new Cell().add(new Paragraph("Is Alcoholic").addStyle(cellStyle)));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(patient.getBoolean("isAlcoholic"))).addStyle(cellStyle)));

        table.addCell(new Cell().add(new Paragraph("Preferred Language").addStyle(cellStyle)));
        table.addCell(new Cell().add(new Paragraph(patient.getString("preferredLanguage")).addStyle(cellStyle)));

        table.addCell(new Cell().add(new Paragraph("Has Insurance").addStyle(cellStyle)));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(patient.getBoolean("hasInsurance"))).addStyle(cellStyle)));

        table.addCell(new Cell().add(new Paragraph("Is Medicaid Eligible").addStyle(cellStyle)));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(patient.getBoolean("isMedicaidEligible"))).addStyle(cellStyle)));

    }

    private static void getIllness(JSONObject patient, Table table, Style cellStyle) {
        JSONArray patientIllnessArray = patient.getJSONArray("patientIllness");
        for (int i = 0; i < patientIllnessArray.length(); i++) {
            JSONObject illness = patientIllnessArray.getJSONObject(i);

            table.addCell(new Cell().add(new Paragraph("Illness ID").addStyle(cellStyle)));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(illness.getInt("illnessId"))).addStyle(cellStyle)));

            table.addCell(new Cell().add(new Paragraph("Description").addStyle(cellStyle)));
            table.addCell(new Cell().add(new Paragraph(illness.getString("description")).addStyle(cellStyle)));

            table.addCell(new Cell().add(new Paragraph("Illnesses").addStyle(cellStyle)));
            JSONArray illnessesArray = illness.getJSONArray("illness");
            StringBuilder illnessesBuilder = new StringBuilder();
            for (int j = 0; j < illnessesArray.length(); j++) {
                illnessesBuilder.append(illnessesArray.getString(j));
                if (j < illnessesArray.length() - 1) {
                    illnessesBuilder.append(", ");
                }
            }
            String illnesses = illnessesBuilder.toString();
            table.addCell(new Cell().add(new Paragraph(illnesses).addStyle(cellStyle)));

            table.addCell(new Cell().add(new Paragraph("Illness Date").addStyle(cellStyle)));
            table.addCell(new Cell().add(new Paragraph(illness.isNull("illnessDate") ? "N/A" : illness.getString("illnessDate")).addStyle(cellStyle)));

        }
    }

    private static void addAppointmentInfo(JSONObject appointment, Table table, Style cellStyle) {
        table.addCell(new Cell().add(new Paragraph("Appointment ID").addStyle(cellStyle)));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(appointment.getInt("appointmentId"))).addStyle(cellStyle)));

        table.addCell(new Cell().add(new Paragraph("Appointment Date").addStyle(cellStyle)));
        table.addCell(new Cell().add(new Paragraph(appointment.getString("appointmentDate")).addStyle(cellStyle)));

        table.addCell(new Cell().add(new Paragraph("Start Time").addStyle(cellStyle)));
        table.addCell(new Cell().add(new Paragraph(appointment.getString("appointmentStartTime")).addStyle(cellStyle)));

        table.addCell(new Cell().add(new Paragraph("End Time").addStyle(cellStyle)));
        table.addCell(new Cell().add(new Paragraph(appointment.getString("appointmentEndTime")).addStyle(cellStyle)));

        table.addCell(new Cell().add(new Paragraph("Status").addStyle(cellStyle)));
        table.addCell(new Cell().add(new Paragraph(appointment.getString("status")).addStyle(cellStyle)));
    }
}