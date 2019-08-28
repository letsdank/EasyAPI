/*
 * Copyright 2019 LetsDank.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.letsdank.easyapi.main;

import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * 
 */
public class PluginLogger {
	private static Logger logger;
	public static boolean infoLogging = true;
	
	public PluginLogger(Logger target) {
		logger = target;
		logger.setFilter(new Filter() {
			@Override
			public boolean isLoggable(LogRecord record) {
				
				//
				// We can report our bugs automatically onto special server
				// and fix.
				//
				
				if (!infoLogging && record.getLevel() == Level.INFO)
					return false;
				
				return true;
			}
		});
	}
	
	public static void error(String msg, Object... params) {
		logger.log(Level.SEVERE, String.format(msg, params));
	}
	
	public static void success(String msg, Object... params) {
		logger.log(Level.INFO, String.format(msg, params));
	}
	
	public static void error(String msg) {
		logger.log(Level.SEVERE, msg);
	}
	
	public static void success(String msg) {
		logger.log(Level.INFO, msg);
	}
}
