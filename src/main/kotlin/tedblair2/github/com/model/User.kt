@file:UseSerializers(LocalDateSerializer::class)
package tedblair2.github.com.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class User(
    val id:Int=0,
    val name:String,
    val address:String,
    val age:Int,
    val dateOfBirth:LocalDate
)

class LocalDateSerializer:KSerializer<LocalDate>{
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("LocalDate",PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalDate {
        return LocalDate.parse(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeString(value.toString())
    }
}
