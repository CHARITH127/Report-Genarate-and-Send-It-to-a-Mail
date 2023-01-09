package reportGen.controller;

import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.activation.DataSource;
import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import java.util.*;

@RestController
@RequestMapping("customer")
@CrossOrigin
public class ReportGenerateController {


    @Autowired
    private JavaMailSender javaMailSender;

    private InputStream imgeSource;

    @GetMapping
    public void generateReport(HttpServletResponse response) {
        try {

            /*parameter map*/
            Map<String, Object> map = new HashMap<>();

            map.put("nameWithIniti", "MRS. L S D PERERA");


            /*set images using inputStream*/
            File initialFile1 = ResourceUtils.getFile("classpath:name of the image or source path");
            InputStream targetStream1 = new FileInputStream(initialFile1);
            map.put("imgUrl", targetStream1);



            Resource report = new ClassPathResource("name of the jrxml file ");

            JasperPrint jasperPrint1 = JasperFillManager.fillReport(report.getInputStream(), map,new JREmptyDataSource(1));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint1, baos);
            DataSource aAttachment =  new ByteArrayDataSource(baos.toByteArray(), "application/pdf");

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true);


            helper.setTo("receiver mail address");



            helper.setFrom("sender mail address");
            helper.setSubject("Testing Email");

            String text = "Testing Email";

            helper.setText(text, false);

            helper.addAttachment("name of the pdf(report.pdf)",aAttachment);

            javaMailSender.send(message);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
