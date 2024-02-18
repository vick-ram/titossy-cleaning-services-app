package com.example.titossycleaningservicesapp.domain.repository
import com.example.titossycleaningservicesapp.data.database.user.data.Customer
import com.example.titossycleaningservicesapp.data.repo.AuthenticationRepository
import com.example.titossycleaningservicesapp.data.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : AuthenticationRepository {
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun loginUser(email: String, password: String): Flow<Resource<Customer>> {
        return flow {
            emit(Resource.Loading())
            val user = Customer(currentUser?.uid ?: "",currentUser?.displayName ?: "", "", "", email, password)
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser =result.user
            if (firebaseUser != null){
                emit(Resource.Success(user))
            }
        }.catch {
            emit(Resource.Failure(it.message.toString()))
        }
    }

    override suspend fun registerUser(userId: String,firstName: String,lastName: String, email: String, password: String): Flow<Resource<Customer>> {
        return flow {
            emit(Resource.Loading())
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user
            firebaseUser?.apply {
                updateProfile(
                    UserProfileChangeRequest.Builder().setDisplayName("$firstName $lastName")
                        .build()
                ).await()
            }
            val user = Customer(firebaseUser?.uid ?: userId, firstName, lastName, email, "", password)
            firebaseUser?.let {
                addUserToDatabase(firebaseUser.uid, user)
            }
            emit(Resource.Success(user))
        }.catch {
            emit(Resource.Failure(it.message.toString()))
        }
    }

    override suspend fun signOut(): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            firebaseAuth.signOut()
            emit(Resource.Success(true))
        }.catch {
            emit(Resource.Failure(it.message.toString()))
        }
    }

    private fun addUserToDatabase(userId: String, user: Customer) {
        fireStore.collection("users").document(userId)
            .set(user)
    }

}
