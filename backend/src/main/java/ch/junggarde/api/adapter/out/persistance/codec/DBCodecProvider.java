package ch.junggarde.api.adapter.out.persistance.codec;

import ch.junggarde.api.model.Appointment;
import ch.junggarde.api.model.image.GalleryImage;
import ch.junggarde.api.model.image.Image;
import ch.junggarde.api.model.media.MetaData;
import ch.junggarde.api.model.member.AdministrativeMember;
import ch.junggarde.api.model.member.Member;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

@SuppressWarnings("ALL")
public class DBCodecProvider implements CodecProvider {
    @Override
    public <T> Codec<T> get(final Class<T> clazz, final CodecRegistry registry) {
        if (clazz.equals(Image.class)) {
            return (Codec<T>) new ImageCodec();
        } else if (clazz.equals(GalleryImage.class)) {
            return (Codec<T>) new GalleryImageCodec();
        } else if (clazz.equals(Member.class)) {
            return (Codec<T>) new MemberCodec();
        } else if (clazz.equals(AdministrativeMember.class)) {
            return (Codec<T>) new AdministrativeMemberCodec();
        } else if (clazz.equals(MetaData.class)) {
            return (Codec<T>) new MediaCodec();
        } else if (clazz.equals(Appointment.class)) {
            return (Codec<T>) new AppointmentCodec();
        }
        return null;
    }
}
