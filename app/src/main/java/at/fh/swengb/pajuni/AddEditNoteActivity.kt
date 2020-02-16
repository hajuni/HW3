package at.fh.swengb.pajuni

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_edit_note.*

class AddEditNoteActivity : AppCompatActivity() {

    companion object {
        val ADDED_EDITED_RESULT = "ADD_OR_EDITED_RESULT"
        val TOKEN = "TOKEN"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)

        val extra: String? = intent.getStringExtra(NoteListActivity.NOTEID)

        if(extra != null){
            val note:Note? = NoteRepository.getNoteById(this, extra)
            if(note != null) {
                idTitle.setText(note.title)
                idText.setText(note.text)
            }
        }
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.note_menu,menu)
        return true
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item?.itemId) {
            R.id.savenote -> {
                    val extra: String? = intent.getStringExtra(NoteListActivity.NOTEID)
                    val sharedPreferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)
                    val token = sharedPreferences.getString(TOKEN, null)

                    if ((extra != null) && (idText.text.toString().isNotEmpty() || idTitle.text.toString().isNotEmpty()) && (token != null)) {
                        val note = Note(extra, idTitle.text.toString(), idText.text.toString(), true)
                        NoteRepository.addNote(this, note)
                        NoteRepository.uplNote( token, note,
                            success = {
                            NoteRepository.addNote(this, it) },
                            error = {
                            Log.e("Upload", it)})

                        val resultIntent = intent
                        resultIntent.putExtra(ADDED_EDITED_RESULT, "ADDED")
                        Log.e("ADD_NOTE", "Added note")
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    } else {
                        Toast.makeText(this, "Fill in both fields!" , Toast.LENGTH_SHORT).show()
                    }
                    true
                }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
