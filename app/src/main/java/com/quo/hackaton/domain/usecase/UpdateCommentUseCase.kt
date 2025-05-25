package com.quo.hackaton.domain.usecase

import com.quo.hackaton.domain.repository.ClientsRepository
import java.util.UUID
import javax.inject.Inject

class UpdateCommentUseCase @Inject constructor(
    private val repo: ClientsRepository
) {
    suspend operator fun invoke(clientId: UUID, comment: String) = repo.updateComment(clientId, comment)
}
