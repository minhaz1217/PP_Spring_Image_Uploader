package io.github.minhaz1217.PP_SPRING_IMAGE_UPLOAD;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface ImageRepository extends PagingAndSortingRepository<Image, Long> {
    public Image findByName(String name);

}
