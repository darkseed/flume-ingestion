/**
 * Copyright (C) 2014 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.ingestion.morphline.checkpointfilter.handler;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.stratio.ingestion.morphline.checkpointfilter.type.CheckpointType;

/**
 * Created by eambrosio on 14/01/15.
 */
public abstract class CheckpointFilterHandler {

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX";
    final String checkpointField;
    CheckpointType checkpointType;
    Map<String, String> context;

    public abstract String getLastCheckpoint(Map<String,String> context);

    public CheckpointFilterHandler(CheckpointType checkpointType, Map<String, String> context) {
        this.checkpointField = checkNotNull(context.get("field"), "Expected non-null checkpoint field");
        this.checkpointType = checkNotNull(checkpointType, "Expected non-null "
                + "checkpoint type");
    }

    public static <T> T getInstance(String fieldName, Class<T> type, Object... parameters) {
        T instance = null;
        try {
            if (parameters != null) {

                instance = type
                        .cast(Class.forName(fieldName).newInstance());
            } else {
                instance = type
                        .cast(Class.forName(fieldName).getDeclaredConstructor(getClasses(parameters)).newInstance
                                (parameters));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return instance;
    }

    private static Class<?>[] getClasses(Object[] parameters) {
        List<Class<?>> classes = new ArrayList<Class<?>>();

        for (int i = 0; i < parameters.length; i++) {
            Object parameter = parameters[i];
            classes.add(parameter.getClass());

        }
        return (Class<?>[]) classes.toArray();
    }
}
