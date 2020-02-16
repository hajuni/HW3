package at.fh.swengb.pajuni

import android.content.Context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object NoteRepository {

    fun getNotes(token: String, lastSync: Long, success:(noteResponse: NotesResponse)->Unit,error:(errorMessage:String)->Unit){
        NoteApi.retrofitService.notes(token, lastSync).enqueue(object : Callback<NotesResponse>{

            override fun onFailure(call: Call<NotesResponse>, t: Throwable) {
                error("The call failed")
            }

            override fun onResponse(call: Call<NotesResponse>, response: Response<NotesResponse>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    success(responseBody)
                } else {
                    error("Error")
                }
            }
        })
    }

    fun uplNote (token: String, noteUpload: Note, success: (note: Note)->Unit, error: (errorMessage: String)->Unit){
        NoteApi.retrofitService.addOrUpdateNote(token, noteUpload).enqueue(object : Callback<Note>{

            override fun onFailure(call: Call<Note>, t: Throwable) {
                error("Problem with the Call" + t.localizedMessage)
            }

            override fun onResponse(call: Call<Note>, response: Response<Note>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    success(responseBody)
                } else {
                    error("Somethings didn't work " + response.message())
                }
            }
        })
    }

    fun addNote(context: Context, newNote: Note) {
        val db = NoteDatabase.getDatabase(context)
        db.NoteDao.insert(newNote)
    }

    fun getNoteById (context: Context, id: String):Note {
        val db = NoteDatabase.getDatabase(context)
        return db.NoteDao.findNoteById(id)
    }

    fun getNotesAll (context: Context):List<Note> {
        val db = NoteDatabase.getDatabase(context)
        return db.NoteDao.getNotesAll()
    }

    fun clearDb (context: Context) {
        val db = NoteDatabase.getDatabase(context)
        return db.NoteDao.deleteAllLessonNote()
    }
}