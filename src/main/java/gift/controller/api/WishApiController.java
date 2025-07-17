package gift.controller.api;

import gift.annotation.LoginMember;
import gift.dto.WishRequestDto;
import gift.dto.WishResponseDto;
import gift.entity.Member;
import gift.entity.Wish;
import gift.service.WishService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/wishes")
public class WishApiController {

    private final WishService wishService;

    public WishApiController(WishService wishService) {
        this.wishService = wishService;
    }

    @PostMapping
    public ResponseEntity<Void> addWish(@Valid @RequestBody WishRequestDto request, @LoginMember Member loginMember) {
        Wish savedWish = wishService.addWish(loginMember, request);
        URI location = URI.create("/api/wishes/" + savedWish.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<Page<WishResponseDto>> getWishes(@LoginMember Member loginMember, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<WishResponseDto> wishes = wishService.getWishesByMember(loginMember, pageable);
        return ResponseEntity.ok(wishes);
    }

    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> deleteWish(@PathVariable Long wishId, @LoginMember Member loginMember) {
        wishService.deleteWish(wishId, loginMember);
        return ResponseEntity.noContent().build();
    }
}