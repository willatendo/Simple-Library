/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package willatendo.simplelibrary.config;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.UnmodifiableConfig;

public interface ConfigSpec<T extends ConfigSpec<T>> extends UnmodifiableConfig {
	default T self() {
		return (T) this;
	}

	void acceptConfig(CommentedConfig commentedConfig);

	boolean isCorrecting();

	boolean isCorrect(CommentedConfig commentedConfig);

	int correct(CommentedConfig commentedConfig);

	void afterReload();
}