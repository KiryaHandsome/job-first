import { useState, useEffect } from 'react';
import './EditResumeModal.css';

const experienceLevels = [
    { value: 'NO_EXPERIENCE', label: 'Без опыта' },
    { value: 'ONE_YEAR', label: '1 год' },
    { value: 'THREE_YEARS', label: '3 года' },
    { value: 'SIX_PLUS_YEARS', label: '6+ лет' }
];

const workTypes = [
    { value: 'REMOTE', label: 'Удаленно' },
    { value: 'OFFICE', label: 'В офисе' },
    { value: 'HYBRID', label: 'Гибридный' }
];

export function EditResumeModal({ isOpen, onClose, onSubmit, resume }) {
    const [formData, setFormData] = useState({
        title: '',
        summary: '',
        isActive: true,
        experience: []
    });

    const [newExperience, setNewExperience] = useState({
        company: '',
        position: '',
        startDate: '',
        endDate: '',
        description: ''
    });

    // Заполняем форму данными резюме при открытии модального окна
    useEffect(() => {
        if (resume) {
            setFormData({
                title: resume.title || '',
                summary: resume.summary || '',
                isActive: resume.isActive !== undefined ? resume.isActive : true,
                experience: resume.experience || []
            });
        }
    }, [resume]);

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: type === 'checkbox' ? checked : value
        }));
    };

    const handleExperienceChange = (e) => {
        const { name, value } = e.target;
        setNewExperience(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const addExperience = () => {
        if (newExperience.company && newExperience.position) {
            setFormData(prev => ({
                ...prev,
                experience: [...prev.experience, { ...newExperience }]
            }));
            setNewExperience({
                company: '',
                position: '',
                startDate: null,
                endDate: null,
                description: ''
            });
        }
    };

    const removeExperience = (index) => {
        setFormData(prev => ({
            ...prev,
            experience: prev.experience.filter((_, i) => i !== index)
        }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        
        // Преобразуем данные в формат для API
        const resumeData = {
            id: resume.id,
            title: formData.title,
            summary: formData.summary || null,
            isActive: formData.isActive,
            experience: formData.experience,
        };

        onSubmit(resumeData);
    };

    if (!isOpen) return null;

    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <div className="modal-header">
                    <h2>Редактирование резюме</h2>
                    <button className="close-button" onClick={onClose}>&times;</button>
                </div>
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="title">Название резюме</label>
                        <input
                            type="text"
                            id="title"
                            name="title"
                            value={formData.title}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="summary">Краткое описание</label>
                        <textarea
                            id="summary"
                            name="summary"
                            value={formData.summary}
                            onChange={handleChange}
                            rows="4"
                        />
                    </div>
                    <div className="form-group">
                        <label className="checkbox-label">
                            <input
                                type="checkbox"
                                name="isActive"
                                checked={formData.isActive}
                                onChange={handleChange}
                            />
                            Показывать всем
                        </label>
                    </div>

                    <div className="experience-section">
                        <h3>Опыт работы</h3>
                        <div className="experience-form">
                            <div className="form-group">
                                <label htmlFor="company">Компания</label>
                                <input
                                    type="text"
                                    id="company"
                                    name="company"
                                    value={newExperience.company}
                                    onChange={handleExperienceChange}
                                />
                            </div>
                            <div className="form-group">
                                <label htmlFor="position">Должность</label>
                                <input
                                    type="text"
                                    id="position"
                                    name="position"
                                    value={newExperience.position}
                                    onChange={handleExperienceChange}
                                />
                            </div>
                            <div className="form-row">
                                <div className="form-group">
                                    <label htmlFor="startDate">Дата начала</label>
                                    <input
                                        type="date"
                                        id="startDate"
                                        name="startDate"
                                        value={newExperience.startDate}
                                        onChange={handleExperienceChange}
                                    />
                                </div>
                                <div className="form-group">
                                    <label htmlFor="endDate">Дата окончания</label>
                                    <input
                                        type="date"
                                        id="endDate"
                                        name="endDate"
                                        value={newExperience.endDate}
                                        onChange={handleExperienceChange}
                                    />
                                </div>
                            </div>
                            <div className="form-group">
                                <label htmlFor="description">Описание</label>
                                <textarea
                                    id="description"
                                    name="description"
                                    value={newExperience.description}
                                    onChange={handleExperienceChange}
                                    rows="3"
                                />
                            </div>
                            <button
                                type="button"
                                className="add-experience-button"
                                onClick={addExperience}
                            >
                                Добавить опыт
                            </button>
                        </div>

                        {formData.experience.length > 0 && (
                            <div className="experience-list">
                                {formData.experience.map((exp, index) => (
                                    <div key={index} className="experience-item">
                                        <div className="experience-header">
                                            <h4>{exp.position} в {exp.company}</h4>
                                            <button
                                                type="button"
                                                className="remove-experience-button"
                                                onClick={() => removeExperience(index)}
                                            >
                                                Удалить
                                            </button>
                                        </div>
                                        <p className="experience-dates">
                                            {exp.startDate} - {exp.endDate || 'По настоящее время'}
                                        </p>
                                        <p className="experience-description">{exp.description}</p>
                                    </div>
                                ))}
                            </div>
                        )}
                    </div>

                    <div className="form-actions">
                        <button type="button" className="cancel-button" onClick={onClose}>
                            Отмена
                        </button>
                        <button type="submit" className="submit-button">
                            Сохранить изменения
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
} 