/**
 * jetbrick-template
 * http://subchen.github.io/jetbrick-template/
 *
 * Copyright 2010-2013 Guoqiang Chen. All rights reserved.
 * Email: subchen@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jetbrick.template.web;

import java.io.File;
import java.util.List;
import javax.servlet.ServletContext;
import jetbrick.template.JetEngine;
import jetbrick.template.resource.FileSystemResource;
import jetbrick.template.resource.Resource;
import jetbrick.template.resource.loader.ResourceLoader;
import jetbrick.template.utils.PathUtils;
import jetbrick.template.utils.finder.TemplateFileFinder;

/**
 * 负责加载 webapp 下面的资源文件.
 *
 * @since 1.1.3
 * @author Guoqiang Chen
 */
public class WebResourceLoader implements ResourceLoader {
    private JetWebEngine engine;
    private ServletContext servletContext;
    private String basepath;
    private String suffix;
    private String encoding;

    @Override
    public void initialize(JetEngine engine, String basepath, String encoding) {
        this.engine = ((JetWebEngine) engine);
        this.servletContext = this.engine.getServletContext();
        this.basepath = PathUtils.getStandardizedTemplateRoot(basepath, false);
        this.suffix = engine.getConfig().getTemplateSuffix();
        this.encoding = encoding;
    }

    @Override
    public Resource load(String name) {
        String pathname = PathUtils.combinePathName(basepath, name, false);
        File file = new File(servletContext.getRealPath(pathname));
        if (!file.exists()) return null;
        return new FileSystemResource(name, file, encoding);
    }

    @Override
    public List<String> loadAll() {
        File dir = new File(servletContext.getRealPath("/"));

        TemplateFileFinder finder = new TemplateFileFinder(suffix);
        finder.lookupFileSystem(dir, true);
        return finder.getResources();
    }
}
