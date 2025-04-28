import React, { useState, useEffect } from 'react';
import { get, post } from '../../utils/api';
import { EditProfileModal } from '../../components/features/profile/EditProfileModal/EditProfileModal';
import './ProfilePage.css';

const ProfilePage = () => {
    const [profile, setProfile] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [isEditModalOpen, setIsEditModalOpen] = useState(false);

    useEffect(() => {
        fetchProfile();
    }, []);

    const fetchProfile = async () => {
        try {
            setLoading(true);
            const response = await post('com.job.user.query.get_user_info');
            console.log('Profile data:', response);
            setProfile(response);
            setError(null);
        } catch (err) {
            console.error('Error fetching profile:', err);
            setError('Failed to load profile data');
        } finally {
            setLoading(false);
        }
    };

    const handleEditClick = () => {
        setIsEditModalOpen(true);
    };

    const handleEditSubmit = async (updatedProfile) => {
        try {
            const response = await post('com.job.user.update_profile', updatedProfile);
            console.log('Profile updated:', response);
            setProfile(response);
            setIsEditModalOpen(false);
        } catch (err) {
            console.error('Error updating profile:', err);
            setError('Failed to update profile');
        }
    };

    if (loading) {
        return <div className="profile-loading">Загрузка...</div>;
    }

    if (error) {
        return <div className="profile-error">{error}</div>;
    }

    if (!profile) {
        return <div className="profile-error">Профиль не найден</div>;
    }

    return (
        <div className="profile-page">
            <div className="profile-header">
                <h1>Профиль пользователя</h1>
                <button className="edit-button" onClick={handleEditClick}>
                    Редактировать профиль
                </button>
            </div>

            <div className="profile-content">
                <div className="profile-section">
                    <h2>Основная информация</h2>
                    <div className="profile-info">
                        <div className="info-item">
                            <span className="info-label">Имя:</span>
                            <span className="info-value">{profile.firstName}</span>
                        </div>
                        <div className="info-item">
                            <span className="info-label">Фамилия:</span>
                            <span className="info-value">{profile.lastName}</span>
                        </div>
                        <div className="info-item">
                            <span className="info-label">Email:</span>
                            <span className="info-value">{profile.email}</span>
                        </div>
                        <div className="info-item">
                            <span className="info-label">Телефон:</span>
                            <span className="info-value">{profile.phone || 'Не указан'}</span>
                        </div>
                        <div className="info-item">
                            <span className="info-label">Город:</span>
                            <span className="info-value">{profile.city || 'Не указан'}</span>
                        </div>
                    </div>
                </div>

                <div className="profile-section">
                    <h2>О себе</h2>
                    <p className="profile-about">{profile.about || 'Информация отсутствует'}</p>
                </div>
            </div>

            <EditProfileModal
                isOpen={isEditModalOpen}
                onClose={() => setIsEditModalOpen(false)}
                onSubmit={handleEditSubmit}
                profile={profile}
            />
        </div>
    );
};

export default ProfilePage; 