package ru.myword.service.controller;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ru.myword.service.bo.Inspection;
import ru.myword.service.bo.StatusInspection;
import ru.myword.service.exception.BaseException;
import ru.myword.service.jms.SenderConfig;
import ru.myword.service.services.DiskFileStorage;
import ru.myword.service.services.InspectionService;

/**
 * Контроллер с логикой Inspection
 *
 * @author Губа Георгий
 */
@RestController
@RequestMapping(InspectionController.BASE_PATH_REQUEST)
public class InspectionController
{
    private static final Logger LOG = LogManager.getLogger(InspectionController.class);

    public static final String BASE_PATH_REQUEST = "/service/inspection";

    private final DiskFileStorage diskFileStorage;
    private final InspectionService inspectionService;
    private final SenderConfig senderConfig;


    @Inject
    public InspectionController(
            final DiskFileStorage diskFileStorage,
            final InspectionService inspectionService,
            final SenderConfig senderConfig)
    {
        this.diskFileStorage = diskFileStorage;
        this.inspectionService = inspectionService;
        this.senderConfig = senderConfig;
    }

    @PostMapping
    public ResponseEntity save(@RequestParam("file") MultipartFile file)
    {
        Inspection inspection = new Inspection();
        inspection.setStatusInspection(StatusInspection.WAIT);
        inspection.setFileName(file.getOriginalFilename());

        try
        {
            String pathFile = diskFileStorage.save(file.getOriginalFilename(), file.getInputStream());
            inspection.setPathToOriginalFile(pathFile);
            inspection = inspectionService.save(inspection);

            senderConfig.sender().send(inspection.getId());
            return ResponseEntity.ok().build();
        }
        catch (IOException e)
        {
            LOG.error("Ошибка получения контента файла " + file.getOriginalFilename(), e);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
    }

    @GetMapping
        public ResponseEntity<List<Inspection>> getList()
    {
        try
        {
            List<Inspection> books = inspectionService.getAll();
            return new ResponseEntity<>(books, HttpStatus.OK);
        }
        catch (Exception ex)
        {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable long id)
    {
        try
        {
            Inspection inspection = inspectionService.getById(id);
            return ResponseEntity.ok(inspection);
        }
        catch (BaseException ex)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}
