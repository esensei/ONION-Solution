package ru.myword.service.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.myword.service.bo.Inspection;
import ru.myword.service.services.InspectionService;

/**
 *
 * @author Губа Георгий
 */
@RestController
@RequestMapping(FileController.BASE_PATH_REQUEST)
public class FileController
{
    public static final String BASE_PATH_REQUEST = "/service/file";

    private final InspectionService inspectionService;

    @Inject
    public FileController(final InspectionService inspectionService)
    {
        this.inspectionService = inspectionService;
    }

    @GetMapping(
            value = "/original/{id}",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getOriginalFile(@PathVariable long id) throws IOException
    {
        Inspection inspection = inspectionService.getById(id);
        String pathFile = inspection.getPathToOriginalFile();
        InputStream in = new FileInputStream(new File(pathFile));
        return IOUtils.toByteArray(in);
    }

    @GetMapping(
            value = "/final/{id}",
            produces = MediaType.IMAGE_JPEG_VALUE

    )
    public byte[] getFinalFile(@PathVariable long id) throws IOException
    {
        Inspection inspection = inspectionService.getById(id);
        String pathFile = inspection.getPathToFinalFile();
        InputStream in = new FileInputStream(new File(pathFile));
        return IOUtils.toByteArray(in);
    }
}
