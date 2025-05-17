import React, {useEffect, useState} from 'react';
import './CompanyPage.css';
import {addUserToCompany, createCompany, editCompany, getCompanyForEmployer} from "../../services/CompanyService.js";
import Notification from "../../components/common/Notification/Notification.jsx";
import {apiCall} from "../../utils/api.js";

const CompanyPage = () => {
    const [company, setCompany] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [isEditing, setIsEditing] = useState(false);
    const [hasCompany, setHasCompany] = useState(false);
    const [showCreateModal, setShowCreateModal] = useState(false);
    const [notification, setNotification] = useState(null);
    const [addEmployerEmail, setAddEmployerEmail] = useState('');

    const [formData, setFormData] = useState({
        id: '',
        name: '',
        description: '',
        websiteLink: '',
        employeesCount: '',
        address: ''
    });

    const [createFormData, setCreateFormData] = useState({
        name: '',
        employeesCount: '',
        description: '',
        address: '',
        websiteLink: ''
    });

    useEffect(() => {
        fetchCompanyInfo();
    }, []);

    const fetchCompanyInfo = async () => {
        try {
            setLoading(true);
            const response = await getCompanyForEmployer();

            if (response?.code === "company.user_not_registered_in_company") {
                setHasCompany(false);
                return;
            }

            console.log("company info: " + JSON.stringify(response));

            setCompany(response);
            setHasCompany(true);
            setFormData({
                id: response.id,
                name: response.name || '',
                description: response.description || '',
                websiteLink: response.websiteLink || '',
                employeesCount: response.employeesCount || '',
                address: response.address || ''
            });
        } catch (error) {
            setError('Failed to load company information');
            console.error('Error fetching company info:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleEditClick = () => {
        setIsEditing(true);
    };

    const handleCancelClick = () => {
        setIsEditing(false);
        setFormData({
            name: company.name || '',
            description: company.description || '',
            website: company.website || '',
            employeesCount: company.employeesCount || '',
            address: company.address || ''
        });
    };

    const handleInputChange = (e) => {
        const {name, value} = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleCreateCompany = () => {
        setShowCreateModal(true);
    };

    const handleCreateFormChange = (e) => {
        const {name, value} = e.target;
        setCreateFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleCreateSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await createCompany({
                ...createFormData,
                employeesCount: createFormData.employeesCount ? parseInt(createFormData.employeesCount) : null
            });

            if (response?.code == null) {
                setShowCreateModal(false);
                setNotification({
                    type: 'success',
                    message: 'Компания успешно создана'
                });
                fetchCompanyInfo();
            } else {
                throw new Error('Failed to create company');
            }
        } catch (error) {
            setNotification({
                type: 'error',
                message: 'Не удалось создать компанию'
            });
            console.error('Error creating company:', error);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await editCompany(formData);
            setCompany(formData);
            setIsEditing(false);
        } catch (error) {
            setError('Failed to update company information');
            console.error('Error updating company info:', error);
        }
    };

    const closeNotification = () => {
        setNotification(null);
    };

    const handleAddUserToCompanySubmit = async (e) => {
        e.preventDefault();
        try {
            const result = await addUserToCompany(addEmployerEmail, company.id)

            if (result.code === 'user.not_found') {
                console.log("user.not_found")
                setNotification({
                    type: 'error',
                    message: 'Пользователь с таким email не зарегистрирован'
                });
            } else if (result.code === 'vacancy.user_already_in_company') {
                setNotification({
                    type: 'error',
                    message: 'Пользователь с таким email зарегистрирован в другой компании'
                });
            } else {
                setNotification({
                    type: 'success',
                    message: 'Пользователь успешно добавлен'
                });
            }

            setAddEmployerEmail('');
        } catch (error) {
            setNotification({
                type: 'error',
                message: 'Не удалось отправить приглашение'
            });
            console.error('Error inviting user:', error);
        }
    };

    if (loading) {
        return <div className="company-page loading">Loading...</div>;
    }

    if (error) {
        return <div className="company-page error">{error}</div>;
    }

    if (!hasCompany) {
        return (
            <div className="no-company-container">
                {notification && (
                    <Notification
                        type={notification.type}
                        message={notification.message}
                        onClose={closeNotification}
                    />
                )}
                <div className="no-company-message">
                    Вы не зарегистрированы в компании. Попросите администратора вашей компании добавить вас в список. В
                    случае если вы первый сотрудник своей компании на нашем сайте, можете создать новую компанию
                </div>
                <button className="create-company-button" onClick={handleCreateCompany}>
                    Создать новую компанию
                </button>

                {showCreateModal && (
                    <div className="modal-overlay">
                        <div className="modal-content">
                            <h2>Создание новой компании</h2>
                            <form onSubmit={handleCreateSubmit}>
                                <div className="form-group">
                                    <label htmlFor="name">Название компании *</label>
                                    <input
                                        type="text"
                                        id="name"
                                        name="name"
                                        value={createFormData.name}
                                        onChange={handleCreateFormChange}
                                        required
                                    />
                                </div>

                                <div className="form-group">
                                    <label htmlFor="employeesCount">Количество сотрудников</label>
                                    <input
                                        type="number"
                                        id="employeesCount"
                                        name="employeesCount"
                                        value={createFormData.employeesCount}
                                        onChange={handleCreateFormChange}
                                        min="1"
                                    />
                                </div>

                                <div className="form-group">
                                    <label htmlFor="description">Описание</label>
                                    <textarea
                                        id="description"
                                        name="description"
                                        value={createFormData.description}
                                        onChange={handleCreateFormChange}
                                        rows="4"
                                    />
                                </div>

                                <div className="form-group">
                                    <label htmlFor="address">Адрес</label>
                                    <input
                                        type="text"
                                        id="address"
                                        name="address"
                                        value={createFormData.address}
                                        onChange={handleCreateFormChange}
                                    />
                                </div>

                                <div className="form-group">
                                    <label htmlFor="websiteLink">Сайт</label>
                                    <input
                                        type="url"
                                        id="websiteLink"
                                        name="websiteLink"
                                        value={createFormData.websiteLink}
                                        onChange={handleCreateFormChange}
                                    />
                                </div>

                                <div className="modal-actions">
                                    <button
                                        type="button"
                                        className="cancel-button"
                                        onClick={() => setShowCreateModal(false)}
                                    >
                                        Отмена
                                    </button>
                                    <button type="submit" className="create-button">
                                        Создать
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                )}
            </div>
        );
    }

    return (
        <div className="company-page">
            {notification && (
                <Notification
                    type={notification.type}
                    message={notification.message}
                    onClose={closeNotification}
                />
            )}
            <div className="company-header">
                <h1>Информация о компании</h1>
                {!isEditing && (
                    <button className="edit-button" onClick={handleEditClick}>
                        Редактировать
                    </button>
                )}
            </div>

            {isEditing ? (
                <form className="company-form" onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="name">Название компании</label>
                        <input
                            type="text"
                            id="name"
                            name="name"
                            value={formData.name}
                            onChange={handleInputChange}
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="description">Описание</label>
                        <textarea
                            id="description"
                            name="description"
                            value={formData.description}
                            onChange={handleInputChange}
                            rows="4"
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="websiteLink">Сайт</label>
                        <input
                            type="url"
                            id="websiteLink"
                            name="websiteLink"
                            value={formData.websiteLink}
                            onChange={handleInputChange}
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="employeesCount">Количество сотрудников</label>
                        <input
                            type="number"
                            id="employeesCount"
                            name="employeesCount"
                            value={formData.employeesCount}
                            onChange={handleInputChange}
                            min="1"
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="address">Адрес</label>
                        <input
                            type="text"
                            id="address"
                            name="address"
                            value={formData.address}
                            onChange={handleInputChange}
                        />
                    </div>

                    <div className="form-actions">
                        <button type="button" className="cancel-button" onClick={handleCancelClick}>
                            Отмена
                        </button>
                        <button type="submit" className="save-button">
                            Сохранить
                        </button>
                    </div>
                </form>
            ) : (
                <div className="company-info">
                    <div className="info-section">
                        <h2>{company.name}</h2>
                        <p className="description">{company.description}</p>
                    </div>

                    <div className="info-grid">
                        <div className="info-item">
                            <span className="label">Сайт:</span>
                            <span className="value">
                                {company.websiteLink ? (
                                    <a href={company.websiteLink} target="_blank" rel="noopener noreferrer">
                                        {company.websiteLink}
                                    </a>
                                ) : (
                                    'Не указан'
                                )}
                            </span>
                        </div>

                        <div className="info-item">
                            <span className="label">Количество сотрудников:</span>
                            <span className="value">{company.employeesCount || 'Не указано'}</span>
                        </div>

                        <div className="info-item">
                            <span className="label">Адрес:</span>
                            <span className="value">{company.address || 'Не указан'}</span>
                        </div>
                    </div>

                    <div className="invite-section">
                        <h3>Добавить сотрудника в вашу компанию</h3>
                        <form onSubmit={handleAddUserToCompanySubmit} className="invite-form">
                            <div className="form-group">
                                <input
                                    type="email"
                                    value={addEmployerEmail}
                                    onChange={(e) => setAddEmployerEmail(e.target.value)}
                                    placeholder="Введите email сотрудника..."
                                    required
                                />
                                <button type="submit" className="invite-button">
                                    Добавить
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default CompanyPage; 