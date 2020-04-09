package ru.myword.service.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ru.myword.service.exception.BaseException;

/**
 * Сервис с базовыми методами поработе с файлами
 *
 * @author Борис Лисков
 */
@Component
public class DiskFileStorage implements FileStorage
{
    @Value("${file.tmpPath}")
    String tmpPathFile;

    @Override
    public String save(String fileName, InputStream initialStream)
    {
        try
        {
            String path = getPathFile(fileName);
            File targetFile = new File(path);

            java.nio.file.Files.copy(
                    initialStream,
                    targetFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);

            IOUtils.closeQuietly(initialStream);

            return path;
        }
        catch (IOException ex)
        {
            throw new BaseException("Ошибка сохранения файла", ex);
        }
    }

    /**
     * Получение пути для файла
     *
     * @param fileName оригинальное имя файла
     * @return итоговый путь для файла
     */
    @Override
    public String getPathFile(String fileName)
    {
        return tmpPathFile + File.separator + System.currentTimeMillis()+ "_" + fileName ;
    }
}
