package ru.myword.service.services;

import java.io.File;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

/**
 * Работа с ФХ
 * @author Борис Лисков
 */
public interface FileStorage
{
    /**
     * Сохранить файл в ФХ
     *
     * @param fileName Имя файла
     * @param initialStream контент
     * @return итоговый путь до файла в ФХ
     */
    String save(String fileName, InputStream initialStream);

    /**
     * Получение пути для файла
     *
     * @param fileName оригинальное имя файла
     * @return итоговый путь для файла
     */
    String getPathFile(String fileName);
}
