package com.codingshuttle.projects.lovable_clone.llm.tools;

import com.codingshuttle.projects.lovable_clone.service.ProjectFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class CodeGenerationTools {

    private final ProjectFileService projectFileService;
    private  final Long projectId;

    @Tool(name = "read_files",
      description = "Read the content of files. Only input the file names present inside the FILE_TREE. DO NOT input any path which is not present unser the FILE_TREE. ")
    public List<String> readFiles(
            @ToolParam(description = "List of relative paths (e.g., ['src/App.tsx'])")
            List<String> paths)
    {
        List<String> result = new ArrayList<>();

        for(String Path: paths)
        {
            String cleanPath= Path.startsWith("/") ? Path.substring(1):Path;
            String content=projectFileService.getFileContent(projectId, cleanPath).content();

            log.info("Requested file: {}", cleanPath);
            result.add(
                    String.format(
                            "--- START OF FILE: %s ---\n%s\n--- END OF FILE ---",
                            cleanPath,content
                    )
            );
        }

       return result;
    }
}
