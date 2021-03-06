/*******************************************************************************
 * Copyright (c) 2012-2016 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.ide.ext.web.html.editor;

import org.eclipse.che.ide.jseditor.client.changeintercept.ChangeInterceptorProvider;
import org.eclipse.che.ide.jseditor.client.changeintercept.TextChangeInterceptor;
import org.eclipse.che.ide.jseditor.client.codeassist.CodeAssistProcessor;
import org.eclipse.che.ide.jseditor.client.editorconfig.AutoSaveTextEditorConfiguration;
import org.eclipse.che.ide.jseditor.client.partition.DocumentPartitioner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The html type editor configuration.
 */
public class HtmlEditorConfiguration extends AutoSaveTextEditorConfiguration {

    /**
     * Auto edit factories.
     */
    private Set<AutoEditStrategyFactory> autoEditStrategyFactories;

    /**
     * Chained processor.
     */
    private DefaultCodeAssistProcessor defaultProcessor;

    /**
     * Build a new Configuration with the given set of strategies.
     * 
     * @param autoEditStrategyFactories the strategy factories
     */
    public HtmlEditorConfiguration(Set<AutoEditStrategyFactory> autoEditStrategyFactories,
                                   DefaultCodeAssistProcessor defaultProcessor) {
        this.autoEditStrategyFactories = autoEditStrategyFactories;
        this.defaultProcessor = defaultProcessor;
    }

    @Override
    public Map<String, CodeAssistProcessor> getContentAssistantProcessors() {
        if (defaultProcessor.getProcessors() == null || defaultProcessor.getProcessors().size() == 0) {
            return null;
        }
        Map<String, CodeAssistProcessor> map = new HashMap<>();
        map.put(DocumentPartitioner.DEFAULT_CONTENT_TYPE, defaultProcessor);
        return map;
    }

    @Override
    public ChangeInterceptorProvider getChangeInterceptorProvider() {
        final ChangeInterceptorProvider parentProvider = super.getChangeInterceptorProvider();
        if (this.autoEditStrategyFactories == null) {
            return parentProvider;
        }
        return new ChangeInterceptorProvider() {
            @Override
            public List<TextChangeInterceptor> getInterceptors(final String contentType) {
                final List<TextChangeInterceptor> result = new ArrayList<>();
                if (parentProvider != null) {
                    final List<TextChangeInterceptor> parentProvided = parentProvider.getInterceptors(contentType);
                    if (parentProvided != null) {
                        result.addAll(parentProvided);
                    }
                }

                for (AutoEditStrategyFactory strategyFactory : autoEditStrategyFactories) {
                    final TextChangeInterceptor interceptor = strategyFactory.build(contentType);
                    result.add(interceptor);
                }
                return result;
            }
        };
    }
}
