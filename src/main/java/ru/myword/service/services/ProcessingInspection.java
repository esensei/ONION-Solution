package ru.myword.service.services;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ru.myword.service.bo.Inspection;
import ru.myword.service.bo.StatusInspection;
import ru.myword.service.exception.BaseException;
import ru.myword.service.security.TokenAuthenticationFilter;

/**
 *
 * @author Борис Лисков
 */
@Component
public class ProcessingInspection
{
    private final static String PREFIX_FINAL_NAME = "final_";
    private final static Logger LOG = LogManager.getLogger(ProcessingInspection.class);

    @Value("${script.execution.timeout}")
    private long executionTimeout;


    private final InspectionService inspectionService;
    private final FileStorage fileStorage;

    @Inject
    public ProcessingInspection(
            final InspectionService inspectionService, final FileStorage fileStorage)
    {
        this.inspectionService = inspectionService;
        this.fileStorage = fileStorage;
    }

    public void exec(long id)
    {
        Inspection inspection = inspectionService.getByIdSystem(id);

        String fileName = inspection.getFileName();
        String pathToOriginalFile = inspection.getPathToOriginalFile();
        String nameForFinalFile = PREFIX_FINAL_NAME + fileName;
        String pathToFinalFile = fileStorage.getPathFile(nameForFinalFile);
        String scriptPath = this.getClass().getClassLoader().getResource("main.py").getPath();

        try
        {
            String script = "python3 %s %s %s";
            long startTime = System.currentTimeMillis();
            Process p = Runtime.getRuntime().exec(String.format(script, scriptPath, pathToOriginalFile,
                    pathToFinalFile));
            while (p.isAlive())
            {
                if (System.currentTimeMillis() > startTime + executionTimeout)
                {
                    throw new BaseException("Скрипт обработки выполнялся слишком долго");
                }
                Thread.sleep(10);
            }
        }
        catch (Exception ex)
        {
            LOG.error("Обработка файла не удалась : " + ex.getMessage());
            inspection.setStatusInspection(StatusInspection.FAIL);
            inspectionService.saveSystem(inspection);
            return;
        }

        inspection.setPathToFinalFile(pathToFinalFile);
        inspection.setStatusInspection(StatusInspection.SUCCESS);

        inspectionService.saveSystem(inspection);
    }
}
