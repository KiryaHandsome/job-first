import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { apiCall } from '../../utils/api';
import { EditResumeModal } from '../../components/features/resumes/EditResumeModal/EditResumeModal';
import './ResumeDetailsPage.css';

const ResumeDetailsPage = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [resume, setResume] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [isEditModalOpen, setIsEditModalOpen] = useState(false);

    useEffect(() => {
        fetchResumeDetails();
    }, [id]);

    const fetchResumeDetails = async () => {
        try {
            setLoading(true);
            const response = await apiCall(`com.job.resume.get_resume_by_id`, { resumeId: id });
            console.log('Resume details:', response);
            setResume(response);
            setError(null);
        } catch (err) {
            console.error('Error fetching resume details:', err);
            setError('Failed to load resume details');
        } finally {
            setLoading(false);
        }
    };

    const handleEditClick = () => {
        setIsEditModalOpen(true);
    };

    const handleEditSubmit = async (updatedResume) => {
        try {
            await apiCall('com.job.resume.update_resume', {
                id: resume.id,
                ...updatedResume
            });
            fetchResumeDetails()
            setIsEditModalOpen(false);
        } catch (err) {
            console.error('Error updating resume:', err);
            setError('Failed to update resume');
        }
    };

    const handleBackClick = () => {
        navigate('/resumes');
    };

    const formatDate = (timestamp) => {
        if (!timestamp) return 'Не указано';
        
        return new Date(timestamp).toLocaleDateString('ru-RU', {
            year: 'numeric',
            month: 'long',
        });
    };

    if (loading) {
        return <div className="loading">Загрузка...</div>;
    }

    if (error) {
        return <div className="error">{error}</div>;
    }

    if (!resume) {
        return <div className="error">Резюме не найдено</div>;
    }

    return (
        <div className="resume-details-page">
            <div className="resume-details-header">
                <button className="back-button" onClick={handleBackClick}>
                    ← Назад к списку
                </button>
                <div className="header-actions">
                    <button className="edit-button" onClick={handleEditClick}>Редактировать</button>
                    <button className="delete-button">Удалить</button>
                </div>
            </div>

            <div className="resume-details-content">
                <div className="resume-main-info">
                    <h1>{resume.title}</h1>
                    <div className="resume-status">
                        Статус: {resume.isActive ? 'Видно всем' : 'Не видно никому'}
                    </div>
                </div>

                <div className="resume-section">
                    <h2>Краткое описание</h2>
                    <p className="resume-summary">{resume.summary || 'Описание отсутствует'}</p>
                </div>

                {resume.experience && resume.experience.length > 0 && (
                    <div className="resume-section">
                        <h2>Опыт работы</h2>
                        <div className="experience-list">
                            {resume.experience.map((exp, index) => (
                                <div key={index} className="experience-item">
                                    <div className="experience-header">
                                        <h3>{exp.position}</h3>
                                    </div>
                                    <div className="company-name">{exp.company}</div>
                                    <div className="experience-dates">
                                        {formatDate(exp.startDate)} - {exp.endDate ? formatDate(exp.endDate) : 'По настоящее время'}
                                    </div>
                                    <p className="experience-description">{exp.description}</p>
                                </div>
                            ))}
                        </div>
                    </div>
                )}

                <div className="resume-section">
                    <h2>Информация о резюме</h2>
                    <div className="resume-info-grid">
                        <div className="info-item">
                            <span className="info-label">ID резюме:</span>
                            <span className="info-value">{resume.id}</span>
                        </div>
                        <div className="info-item">
                            <span className="info-label">ID пользователя:</span>
                            <span className="info-value">{resume.userId}</span>
                        </div>
                        <div className="info-item">
                            <span className="info-label">Дата создания:</span>
                            <span className="info-value">{formatDate(resume.createdAtMillis)}</span>
                        </div>
                        <div className="info-item">
                            <span className="info-label">Последнее обновление:</span>
                            <span className="info-value">{formatDate(resume.editedAtMillis)}</span>
                        </div>
                    </div>
                </div>
            </div>

            <EditResumeModal
                isOpen={isEditModalOpen}
                onClose={() => { setIsEditModalOpen(false)}}
                onSubmit={handleEditSubmit}
                resume={resume}
            />
        </div>
    );
};

export default ResumeDetailsPage; 