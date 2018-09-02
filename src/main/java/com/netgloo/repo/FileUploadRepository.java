/*
 * Copyright 2011-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netgloo.repo;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.netgloo.models.FileUpload;
import com.netgloo.models.LutMenu;
import com.netgloo.models.LutUser;

/**
 * Repository to manage {@link Account} instances.
 * 
 * @author Oliver Gierke
 */
public interface FileUploadRepository extends CrudRepository<FileUpload, Long> {
	@Query("SELECT t FROM FileUpload t where t.filename = ?1") 
    FileUpload findByFilename(String filename);
}
